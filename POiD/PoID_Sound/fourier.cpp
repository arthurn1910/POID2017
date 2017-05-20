#define _USE_MATH_DEFINES
#include "fourier.h"
#include <cmath>
#include "persistence1d/persistence1d.hpp"

Fourier::Fourier(QObject *parent) : QObject(parent)
{
    logThreashold = 8;
    windowType = 0;
    windowSizeMulti = 16384;
    frequencyThreshold = 10;
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
    int windowSize = 1;

    while (windowSize * 2 < inputData.size())
        windowSize *= 2;

    qDebug() << "Detected window size: " << windowSize;

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

    FFT(fourierComplexData, windowSize);

    fourierAmplitudeData.clear();
    for (int i = 0; i < windowSize / 2; i++) {
        fourierAmplitudeData.push_back(std::log(std::abs(fourierComplexData[i])));
    }

    fourierDurationInSeconds = fourierAmplitudeData.size() * 2.0 / (double) inputData.size();

    mostSimilar = (sampleRate / windowSize) * findMostSimilar(fourierAmplitudeData, windowSize);
//    median = (sampleRate / windowSize) * findMaxMedian(fourierAmplitudeData);
    qDebug() << "Most similiar: " << mostSimilar;

    updateFourierChart();
    emit fourierFinished();
}


double Fourier::findMostSimilar(std::vector<float> vec, int windowSize)
{
    p1d::Persistence1D p;
    p.RunPersistence(vec);
    std::vector< p1d::TPairedExtrema > Extrema;
    p.GetPairedExtrema(Extrema, logThreashold);

    QVector<int> maxVector;
    QVector<int> similarCount;
    int max = 0;

    for(std::vector< p1d::TPairedExtrema >::iterator it = Extrema.begin(); it != Extrema.end(); it++)
    {
        maxVector.append((*it).MaxIndex);
        similarCount.append(0);
    }

    qSort(maxVector);

    for (int i = 0; i < maxVector.size(); i++) {
        for (int j = i + 1; j < maxVector.size(); j++) {
            int comparable = (sampleRate / windowSize) * (maxVector.at(j) + 0.5);
            int compareTo = (sampleRate / windowSize) * (maxVector.at(i) + 0.5);
            if ((comparable % compareTo) <= compareTo * 0.1 ||
                (comparable % compareTo) >= compareTo * 0.9) {
                similarCount.replace(i, similarCount.at(i) + 1);

                if (max < similarCount.at(i))
                    max = similarCount.at(i);
            }
        }
    }

    for (int i = 0; i < similarCount.size(); i++) {
        if (similarCount.at(i) == max) {
            return maxVector.at(i);
        }
    }

    return -1;
}

double Fourier::findMaxMedian(std::vector<float> vec)
{
    p1d::Persistence1D p;
    p.RunPersistence(vec);
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
        return maxVector.at(maxVector.size() / 2);
    }

    return -1;
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

    if (samples > 600) {
        offset = samples / 600;
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

QString Fourier::getBasicFrequency()
{
    return QString::number(mostSimilar, 'f', 2);
}

QString Fourier::getAmplitude()
{
    return QString::number(amplitude, 'f', 2);
}

void Fourier::setWindowParam(QString value)
{
    windowSizeMulti = value.toInt();

    if (!((windowSizeMulti != 0) && ((windowSizeMulti & (windowSizeMulti - 1)) == 0))) {
        windowSizeMulti = 512;
    }
}

void Fourier::setFrequencyThreshold(QString value)
{
    frequencyThreshold = value.toDouble();
    if (frequencyThreshold < 0)
        frequencyThreshold = 0;
}

void Fourier::runFOURIERmultiTone()
{
    qDebug() << "Starting fourier multi";

    sequenceFrequency.clear();
    double lastFrequency = -1;

    for (int iter = 0; iter + windowSizeMulti < inputData.size(); iter += windowSizeMulti) {
        double windowFrequency = 0;
        std::vector<float> windowAmplitudeData;
        std::complex<float> *windowComplexData = new std::complex<float>[windowSizeMulti];

        for (int i = 0; i < windowSizeMulti; i++) {
            windowComplexData[i] = inputData.at(iter + i) * windowBarlett(i, windowSizeMulti);
        }

        FFT(windowComplexData, windowSizeMulti);

        windowAmplitudeData.clear();
        for (int i = 0; i < windowSizeMulti / 2; i++) {
            windowAmplitudeData.push_back(std::log(std::abs(windowComplexData[i])));
        }
        delete [] windowComplexData;

        windowFrequency = (sampleRate / windowSizeMulti) * findMostSimilar(windowAmplitudeData, windowSizeMulti);

        if (lastFrequency == -1) {
            sequenceFrequency.push_back(iter); // start first
            lastFrequency = windowFrequency;
            sequenceFrequency.push_back(windowFrequency); // starting freq
            continue;
        }

        if (std::fabs(windowFrequency - lastFrequency) / lastFrequency < frequencyThreshold / 100.0) {

        } else {
            sequenceFrequency.push_back(iter); // end last
            sequenceFrequency.push_back(iter); // start next
            sequenceFrequency.push_back(windowFrequency); // next freq
            lastFrequency = windowFrequency; // new freq for segment
        }
    }
    sequenceFrequency.push_back(inputData.size()); // start next

    for (int i = 0; i < sequenceFrequency.size(); i += 3) {
        qDebug() << "From: " << sequenceFrequency.at(i) / sampleRate << ", To: " << sequenceFrequency.at(i + 2) / sampleRate
                 << ", Freq: " << sequenceFrequency.at(i + 1);
    }

    int amplitudeOffset = windowSizeMulti / 16;
    sequenceAmplitude.clear();

    for (int iter = 0; iter + amplitudeOffset < inputData.size(); iter += amplitudeOffset) {
        int maxAmplitude = 0;
        for (int i = 0; i < amplitudeOffset; i++) {
            if (maxAmplitude < std::abs(inputData.at(iter + i)))
                maxAmplitude = std::abs(inputData.at(iter + i));
        }

        sequenceAmplitude.push_back(maxAmplitude);
    }
    sequenceAmplitude.push_back(0);
}


std::vector<double> Fourier::getSequenceFrequency()
{
    return sequenceFrequency;
}

std::vector<double> Fourier::getSequenceAmplitude()
{
    return sequenceAmplitude;
}
