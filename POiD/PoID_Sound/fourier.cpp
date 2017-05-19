#define _USE_MATH_DEFINES
#include "fourier.h"
#include <cmath>
#include "persistence1d/persistence1d.hpp"

Fourier::Fourier(QObject *parent) : QObject(parent)
{
    logThreashold = 1;
    windowType = 0;
}

void Fourier::FFT(std::complex<float> *input, const int size)
{
    if (size <= 1)
        return;

    // divide
    std::complex<float> *even = new std::complex<float>[size/2];
    std::complex<float>  *odd = new std::complex<float>[size/2];
    int evenCounter = 0;
    int oddCounter = 0;
    for (int i = 0; i < size; i++) {
        if (i % 2 == 0) {
            even[evenCounter++] = input[i];
        } else {
            odd[oddCounter++] = input[i];
        }
    }

    // conquer
    FFT(even, size/2);
    FFT(odd, size/2);

    // combine
    for (int k = 0; k < size/2; k++)
    {
        std::complex<float> complexNumber(0, -2 * M_PI * k / size);
        //complexNumber.real(0);
        //complexNumber.imag(-2 * M_PI * k / N);
        std::complex<float> t = std::exp(complexNumber) * odd[k];
        input[k    ] = even[k] + t;
        input[k+size/2] = even[k] - t;
    }

    delete [] even;
    delete [] odd;
}

void Fourier::IFFT(std::complex<float> *input, const int size)
{
    for (int i = 0; i < size; i++) {
        input[i] = std::conj(input[i]);
    }

    // forward fft
    FFT(input, size);

    for (int i = 0; i < size; i++) {
        input[i] = std::conj(input[i]);
    }

    for (int i = 0; i < size; i++) {
        input[i] /= size;
    }
}

void Fourier::runFOURIER()
{
    amplitude = 0;
    int windowSize = 32768;
    fourierComplexData = new std::complex<float>[windowSize];
    for (int i = 0; i < windowSize; i++) {
        switch (windowType) {
        case 0:
            fourierComplexData[i] = inputData.at(i) * windowGauss(i, windowSize);
            break;
        case 1:
            fourierComplexData[i] = inputData.at(i) * windowHanningOne(i, windowSize);
            break;
        case 2:
            fourierComplexData[i] = inputData.at(i) * windowHanningTwo(i, windowSize);
            break;
        case 3:
            fourierComplexData[i] = inputData.at(i) * windowBarlett(i, windowSize);
            break;
        }
        if (amplitude < std::fabs(inputData.at(i)))
            amplitude = std::fabs(inputData.at(i));
    }

    qDebug() << "Running Fourier: " << windowSize;
    FFT(fourierComplexData, windowSize);
    qDebug() << "Fourier done";

    fourierAmplitudeData.clear();
    for (int i = 0; i < windowSize / 2; i++) {
        fourierAmplitudeData.push_back(std::log(std::abs(fourierComplexData[i])));
    }

    fourierDurationInSeconds = fourierAmplitudeData.size() * 2.0 / (double) inputData.size();


    p1d::Persistence1D p;
    p.RunPersistence(fourierAmplitudeData);
    std::vector< p1d::TPairedExtrema > Extrema;
    p.GetPairedExtrema(Extrema, logThreashold);

    QVector<int> maxVector;

    for(std::vector< p1d::TPairedExtrema >::iterator it = Extrema.begin(); it != Extrema.end(); it++)
    {
        maxVector.append((*it).MaxIndex);
    }

    qSort(maxVector);

    for (int i = maxVector.size() - 1; i >= 1; i--) {
        maxVector.replace(i, maxVector.at(i) - maxVector.at(i - 1));
    }
    qSort(maxVector);
    if (maxVector.size() > 0) {
        median = (sampleRate / windowSize) * maxVector.at(maxVector.size() / 2);
        qDebug() << "Median: " << median;
    }

    updateFourierChart();
    emit fourierFinished();
}

void Fourier::initFourierChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis)
{
    fourierSeries = lineSeries;
    fourierXAxis = xAxis;
    fourierYAxis = yAxis;
}

void Fourier::setData(DataSource *data)
{
    fourierMagnitude = 1;
    fourierOffset = 0;

    inputData = data->getInputData();
    sampleRate = data->getSampleRate();
}

void Fourier::updateFourierChart()
{
    qDebug() << "Updating Fourier chart";
    fourierSeries->clear();
    double dataSize = fourierAmplitudeData.size(); // 32768; // inputData.size();

    double maxY = 0;
    double minX = fourierOffset * fourierDurationInSeconds * (1.0 / fourierMagnitude);
    double maxX = minX + fourierDurationInSeconds * (1.0 / fourierMagnitude);

    int offset;
    int samples = dataSize * (1.0 / fourierMagnitude);

    if (samples > 200) {
        offset = samples / 200;
    } else {
        offset = 1;
    }

    for (int i = minX * dataSize; i < maxX * dataSize; i += offset) {
        //qDebug() << (44100.0 / (dataSize * 2)) * i << ", " << fourierAmplitudeData.at(i);
        fourierSeries->append(i / dataSize, fourierAmplitudeData.at(i));
        if (fourierAmplitudeData.at(i) > maxY)
            maxY = fourierAmplitudeData.at(i);
    }

    fourierXAxis->setMin(minX);
    fourierXAxis->setMax(maxX);

    fourierYAxis->setMin(-10);
    fourierYAxis->setMax(maxY * 1.1);
}

void Fourier::increaseFourierMagnitude()
{
    fourierMagnitude++;
    updateFourierChart();
}

void Fourier::decreaseFourierMagnitude()
{
    if (fourierMagnitude > 1) {
        fourierMagnitude--;
        if (fourierMagnitude == 1)
            fourierOffset = 0;
        else
            fourierOffsetChanged(fourierOffset);
        updateFourierChart();
    }
}

void Fourier::fourierOffsetChanged(double value)
{
    if (fourierMagnitude > 1) {
        fourierOffset = value * (fourierMagnitude - 1);
        updateFourierChart();
    }
}

QString Fourier::getThreasholdParam()
{
    return QString::number(logThreashold, 'f', 0);
}

void Fourier::setThreasholdParam(QString value)
{
    logThreashold = value.toDouble();
    if (logThreashold < 0)
        logThreashold = 0;
}

void Fourier::setWindowType(QString type)
{
    if (type == "Gaussa") {
        windowType = 0;
    } else if (type == "Hamming 1") {
        windowType = 1;
    } else if (type == "Hamming 2") {
        windowType = 2;
    } else if (type == "Bartlett") {
        windowType = 3;
    } else {
        windowType = 0;
    }
}

double Fourier::windowGauss(int iter, int all)
{
    double output = 0;
    double up = 0;
    double down = 0;

    up = (iter - (all - 1)) / 2.0;
    down = (all - 1) / 2.0;

    output = std::exp(-0.5 * std::pow(up / down, 2));

    return output;
}

double Fourier::windowHanningOne(int iter, int all)
{
    double output = 0;

    output = 0.53836 - 0.46164 * std::cos((2.0 * M_PI * iter) / (all - 1));

    return output;
}

double Fourier::windowHanningTwo(int iter, int all)
{
    double output = 0;

    output = 0.5 * (1 - std::cos((2.0 * M_PI * iter) / (all - 1)));

    return output;
}

double Fourier::windowBarlett(int iter, int all)
{
    double output = 0;
    double tempFirst = 0;
    double tempSecond = 0;
    double tempThird = 0;

    tempFirst = 2.0 / (all - 1);
    tempSecond = (all - 1) / 2.0;
    tempThird = std::fabs(iter - tempSecond);

    output = tempFirst * (tempSecond - tempThird);

    return output;
}


QString Fourier::getBasicFrequencyMedian()
{
    return QString::number(median, 'f', 2);
}

QString Fourier::getAmplitude()
{
    return QString::number(amplitude, 'f', 2);
}
