#define _USE_MATH_DEFINES
#include "filter.h"
#include <cmath>
#include "persistence1d/persistence1d.hpp"

Filter::Filter(QObject *parent) : QObject(parent)
{
    windowN = 1024;
    windowM = 512;
    hopSizeR = 512;
    fc = 500;
    filterL = 512;
    windowType = 0;
}

void Filter::FFT(std::complex<float> *input, const int size)
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

void Filter::IFFT(std::complex<float> *input, const int size)
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

void Filter::initFilterChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis)
{
    filterSeries = lineSeries;
    filterXAxis = xAxis;
    filterYAxis = yAxis;
}

void Filter::setData(DataSource *data)
{
    filterMagnitude = 1;
    filterOffset = 0;

    inputData = data->getInputData();
}

void Filter::updateFilterChart()
{
    qDebug() << "Updating filter chart";
    filterSeries->clear();
    double dataSize = outputData.size();
//    qDebug() << "Data size: " << dataSize << " vs " << inputData.size();

    filterDurationInSeconds = dataSize / 44100.0;
    double minY = 0;
    double maxY = 0;
    double minX = filterOffset * filterDurationInSeconds * (1.0 / filterMagnitude);
    double maxX = minX + filterDurationInSeconds * (1.0 / filterMagnitude);

    int offset = 1;
    int samples = dataSize * (1.0 / filterMagnitude);

    if (samples > 5000) {
        offset = samples / 5000;
    } else {
        offset = 1;
    }

    QList<QPointF> pointList;
    for (int i = minX * 44100; i < maxX * 44100; i += offset) {
//        qDebug() << i << " / " << maxX * 44100;
        pointList.append(QPointF(i / 44100.0, filterData.at(i)));
        if (filterData.at(i) > maxY)
            maxY = filterData.at(i);
        if (filterData.at(i) < minY)
            minY = filterData.at(i);
    }
    filterSeries->replace(pointList);

    filterXAxis->setMin(minX);
    filterXAxis->setMax(maxX);

//    QList<QPointF> pointList;
//    for (int i = 0; i < filterData.size(); i += 1) {
//        pointList.append(QPointF(i, filterData.at(i)));
//        if (filterData.at(i) > maxY)
//            maxY = filterData.at(i);
//        if (filterData.at(i) < minY)
//            minY = filterData.at(i);
//    }
//    filterSeries->replace(pointList);

//    filterXAxis->setMin(0);
//    filterXAxis->setMax(filterData.size());


    filterYAxis->setMin(minY * 1.1);
    filterYAxis->setMax(maxY * 1.1);
}

void Filter::increaseFilterMagnitude()
{
    filterMagnitude++;
    updateFilterChart();
}

void Filter::decreaseFilterMagnitude()
{
    if (filterMagnitude > 1) {
        filterMagnitude--;
        if (filterMagnitude == 1)
            filterOffset = 0;
        else
            filterOffsetChanged(filterOffset);
        updateFilterChart();
    }
}

void Filter::filterOffsetChanged(double value)
{
    if (filterMagnitude > 1) {
        filterOffset = value * (filterMagnitude - 1);
        updateFilterChart();
    }
}

void Filter::runFilter()
{
    filterData.clear();

    qDebug() << "Step 1";
    QList<std::complex<float> *> samples;
    for (int i = 0; i < inputData.size() - windowM; i += hopSizeR) {
        std::complex<float> *currentWindow = new std::complex<float>[windowN];
        for (int j = 0; j < windowM; j++) {
            currentWindow[j] = inputData.at(i + j) * windowValue(j, windowM);
        }
        for (int j = windowM; j < windowN; j++) {
            currentWindow[j] = 0;
        }

        samples.append(currentWindow);
    }

    qDebug() << "Step 2";
    QList<double> filter = createFilter();

    qDebug() << "Step 3";
    for (int i = 0; i < filter.length(); i++) {
        filter.replace(i, filter.at(i) * windowValue(i, filter.length()));
    }
    for (int i = filter.length(); i < windowN; i++) {
        filter.append(0);
    }

    qDebug() << "Step 4";
    std::complex<float> *filterFourier = new std::complex<float>[windowN];
    for (int i = 0; i < filter.length(); i++) {
        filterFourier[i] = filter.at(i);
    }
    FFT(filterFourier, windowN);

    for (int i = 0; i < samples.size(); i++) {
        std::complex<float> *window = samples.at(i);
        FFT(window, windowN);
        for (int j = 0; j < windowN; j++) {
            window[j] *= filterFourier[j];
        }
    }

    qDebug() << "Step 5";
    outputData.clear();
    for (int i = 0; i < samples.size() * windowM + (windowN - windowM); i++) {
        outputData.append(0);
    }

    for (int i = 0; i < samples.size(); i++) {
        std::complex<float> *window = samples.at(i);
        IFFT(window, windowN);
        for (int j = 0; j < windowN; j++) {
            outputData.replace(i * hopSizeR + j, window[j].real() + outputData.at(i * hopSizeR + j));
        }

    }

    filterData.clear();
    for (int i = 0; i < outputData.size(); i++) {
        filterData.push_back(outputData.at(i));
    }

    updateFilterChart();
    emit filterFinished();
}


void Filter::runFilterSplot()
{
    filterData.clear();
    outputData.clear();

    QList<double> filter = createFilter();

    for (int i = 0; i < inputData.size() + filterL; i++) {
        double temp = 0;

        for (int j = 0; j < filterL; j++) {
            double x;
            if (i - j < 0 || i - j >= inputData.size())
                x = 0;
            else
                x = inputData.at(i - j);
            temp += x * filter.at(j);
        }

        outputData.append(temp);
    }

    filterData.clear();
    for (int i = 0; i < outputData.size(); i++) {
        filterData.push_back(outputData.at(i));
    }

    updateFilterChart();
    emit filterFinished();
}

void Filter::runEqualizer(double one, double two, double three, double four, double five)
{
    qDebug() << "20Hz: " << one;
    qDebug() << "80Hz: " << two;
    qDebug() << "320Hz: " << three;
    qDebug() << "1280Hz: " << four;
    qDebug() << "5120Hz: " << five;


    filterData.clear();

    qDebug() << "Step 1";
    QList<std::complex<float> *> samples;
    windowType = 0; // rectangle window
    for (int i = 0; i < inputData.size() - windowN; i += hopSizeR) {
        std::complex<float> *currentWindow = new std::complex<float>[windowN];
        for (int j = 0; j < windowM; j++) {
            currentWindow[j] = inputData.at(i + j) * windowValue(j, windowM);
        }
        for (int j = windowM; j < windowN; j++) {
            currentWindow[j] = 0;
        }

        samples.append(currentWindow);
    }

    QList<std::complex<float> *> samplesForFreq;
    QList<double> freqForSamples;
    windowType = 2; // von hann window
    for (int i = 0; i < inputData.size() - windowN; i += windowN) {
        std::complex<float> *currentWindow = new std::complex<float>[windowN];
        for (int j = 0; j < windowN; j++) {
            currentWindow[j] = inputData.at(i + j) * windowValue(j, windowN);
        }
        for (int j = windowN; j < windowN; j++) {
            currentWindow[j] = 0;
        }

        samplesForFreq.append(currentWindow);
    }

    qDebug() << "Calc freq";
    for (int i = 0; i < samplesForFreq.size(); i++) {
        std::complex<float> *window = samplesForFreq.at(i);
        std::vector<float> windowAmplitudeData;
        FFT(window, windowN);

        for (int j = 0; j < windowN / 2; j++) {
            windowAmplitudeData.push_back(std::log(std::abs(window[j])));
        }
        double windowFrequency = (44100.0 / windowN) * findMostSimilar(windowAmplitudeData, windowN / 2);
        freqForSamples.append(windowFrequency);
//        qDebug() << "Freq: " << windowFrequency << " Hz";
    }

    qDebug() << "Samples: " << samples.size();
    qDebug() << "Samples for freq: " << samplesForFreq.size();
    qDebug() <<" Freq for sample: " << freqForSamples.size();

    qDebug() << "Calc gain";
    for (int i = 0; i < samples.size(); i++) {
        std::complex<float> *window = samples.at(i);
        FFT(window, windowN);

        double windowFrequency = freqForSamples.at(i / 2);
        int index = (int) windowFrequency / (44100.0 / windowN);

        float gain = 0;

        if (windowFrequency >= 20 && windowFrequency < 80) {
            gain = one;
        } else if (windowFrequency >= 80 && windowFrequency < 320) {
            gain = two;
        } else if (windowFrequency >= 320 && windowFrequency < 1280) {
            gain = three;
        } else if (windowFrequency >= 1280 && windowFrequency < 5120) {
            gain = four;
        } else if (windowFrequency >= 5120 && windowFrequency < 20480) {
            gain = five;
        }

        if (gain >= 0)
            gain += 1;
        else
            gain = 1 / std::fabs(gain);

        for (int j = 1; j < windowN - 2; j++) {
            window[j] *= gain;
        }
//        window[index] *= gain;
//        window[windowN - 1 - index] *= gain;
    }

    qDebug() << "Step 5";
    outputData.clear();
    for (int i = 0; i < samples.size() * windowM + (windowN - windowM); i++) {
        outputData.append(0);
    }

    for (int i = 0; i < samples.size(); i++) {
        std::complex<float> *window = samples.at(i);
        IFFT(window, windowN);
        for (int j = 0; j < windowN; j++) {
            outputData.replace(i * hopSizeR + j, window[j].real() + outputData.at(i * hopSizeR + j));
        }

    }

    filterData.clear();
    for (int i = 0; i < outputData.size(); i++) {
        filterData.push_back(outputData.at(i));
    }

    updateFilterChart();
    emit filterFinished();
}

double Filter::findMostSimilar(std::vector<float> vec, int windowSize)
{
    double sampleRate = 44100;
    p1d::Persistence1D p;
    p.RunPersistence(vec);
    std::vector< p1d::TPairedExtrema > Extrema;
    p.GetPairedExtrema(Extrema, 4);

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

QVector<qint16> Filter::getSoundData()
{
    return outputData;
}

QString Filter::getAmplitude()
{
    return QString::number(amplitude, 'f', 2);
}


void Filter::setWindowType(QString type)
{
    if (type == "Rectangle") {
        windowType = 0;
    } else if (type == "Hamming") {
        windowType = 1;
    } else if (type == "von Hann") {
        windowType = 2;
    } else {
        windowType = 0;
    }
}

double Filter::windowValue(int iter, int windowSize)
{
    if (windowType == 0) {
        if (iter < windowSize)
            return 1;
        else
            return 0;
    } else if (windowType == 1) {
        if (iter == 0 || iter == windowSize - 1)
            return 0.5 * (0.5 - 0.5 * std::cos((2 * M_PI * iter) / (windowSize - 1)));
        else
            return 0.5 - 0.5 * std::cos((2 * M_PI * iter) / (windowSize - 1));
    } else if (windowType == 2) {
        if (iter == 0 || iter == windowSize - 1)
            return 0.5 * (0.54 - 0.46 * std::cos((2 * M_PI * iter) / (windowSize - 1)));
        else
            return 0.54 - 0.46 * std::cos((2 * M_PI * iter) / (windowSize - 1));
    }
}

void Filter::setWindowParam(QString value)
{
    windowN = value.toInt();
}

void Filter::setWindowMParam(QString value)
{
    windowM = value.toInt();
}
void Filter::setHopsizeParam(QString value)
{
    hopSizeR = value.toInt();
}
void Filter::setFcParam(QString value)
{
    fc = value.toInt();
}
void Filter::setLengthParam(QString value)
{
    filterL = value.toInt();
}

QList<double> Filter::createFilter()
{
    qDebug() << "Making filter params";
    double halfOfLenght = (filterL - 1) / 2;
    double topConstParam = (2 * M_PI * fc) / 44100.0;
    QList<double> output;

    for (int i = 0; i < filterL; i++) {
        if (i == (int) halfOfLenght) {
            output.append(2 * fc / 44100.0);
        } else {

            double top = std::sin(topConstParam * (i - halfOfLenght));

            double bottom = M_PI * (i - halfOfLenght);

            output.append(top / bottom);
        }
    }

    return output;
}
