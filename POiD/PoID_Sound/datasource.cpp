#include "datasource.h"
#include <QtCharts/QXYSeries>
#include <QtCharts/QAreaSeries>
#include <QtQuick/QQuickView>
#include <QtQuick/QQuickItem>
#include <QtCore/QDebug>
#include <QtCore/QtMath>
#include <QtEndian>
#include <QDataStream>

QT_CHARTS_USE_NAMESPACE

Q_DECLARE_METATYPE(QAbstractSeries *)
Q_DECLARE_METATYPE(QAbstractAxis *)


// array.replace(44, 2, QByteArray::fromRawData(new char[2] {test & 0xFF, test >> 8}, 2));
// outputFile.replace(24, 4, QByteArray::fromRawData((const char*)&test, 4));

DataSource::DataSource(QObject *parent) : QObject(parent)
{
    inputAudioFormat = new QAudioFormat();
    outputAudioFormat = new QAudioFormat();
    inputBuffer = new QBuffer(&mediaplayer);
    outputBuffer = new QBuffer(&mediaplayer);
}

void DataSource::initInputChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis)
{
    inputSeries = lineSeries;
    inputXAxis = xAxis;
    inputYAxis = yAxis;
}

void DataSource::initOutputChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis)
{
    outputSeries = lineSeries;
    outputXAxis = xAxis;
    outputYAxis = yAxis;
}

void DataSource::loadSoundData(QString path)
{
    inputMagnitude = 1;
    inputOffset = 0;

    outputMagnitude = 1;
    outputOffset = 0;

    readWAV(path.mid(8));

    updateInputChart();
    updateOutputChart();

    emit dataLoaded();
}

void DataSource::updateInputChart()
{
    inputSeries->clear();
    double sampleRate = inputAudioFormat->sampleRate();

    double minY = 0;
    double maxY = 0;
    double minX = inputOffset * inputDurationInSeconds * (1.0 / inputMagnitude);
    double maxX = minX + inputDurationInSeconds * (1.0 / inputMagnitude);

    int offset;
    int samples = sampleRate * (1.0 / inputMagnitude);

    if (samples > 200) {
        offset = samples / 200;
    } else {
        offset = 1;
    }

    for (int i = minX * sampleRate; i < maxX * sampleRate; i += offset) {
        inputSeries->append(i / sampleRate, inputData.at(i));
        if (inputData.at(i) > maxY)
            maxY = inputData.at(i);
        else if (inputData.at(i) < minY)
            minY = inputData.at(i);
    }

    inputXAxis->setMin(minX);
    inputXAxis->setMax(maxX);

    if (abs(maxY) > abs(minY)) {
        inputYAxis->setMin(-abs(maxY * 1.2));
        inputYAxis->setMax(abs(maxY * 1.2));
    } else {
        inputYAxis->setMin(-abs(minY * 1.2));
        inputYAxis->setMax(abs(minY * 1.2));
    }
}

void DataSource::updateOutputChart()
{
    outputSeries->clear();
    double sampleRate = outputAudioFormat->sampleRate();

    double minY = 0;
    double maxY = 0;
    double minX = outputOffset * outputDurationInSeconds * (1.0 / outputMagnitude);
    double maxX = minX + outputDurationInSeconds * (1.0 / outputMagnitude);

    int offset;
    int samples = sampleRate * (1.0 / outputMagnitude);

    if (samples > 200) {
        offset = samples / 200;
    } else {
        offset = 1;
    }

    for (int i = minX * sampleRate; i < maxX * sampleRate; i += offset) {
        outputSeries->append(i / sampleRate, outputData.at(i));
        if (outputData.at(i) > maxY)
            maxY = outputData.at(i);
        else if (outputData.at(i) < minY)
            minY = outputData.at(i);
    }

    outputXAxis->setMin(minX);
    outputXAxis->setMax(maxX);

    if (abs(maxY) > abs(minY)) {
        outputYAxis->setMin(-abs(maxY * 1.2));
        outputYAxis->setMax(abs(maxY * 1.2));
    } else {
        outputYAxis->setMin(-abs(minY * 1.2));
        outputYAxis->setMax(abs(minY * 1.2));
    }
}

void DataSource::playInput()
{
    inputBuffer->setBuffer(&inputFile);
    if (!inputBuffer->isOpen())
        inputBuffer->open(QIODevice::ReadOnly);
    mediaplayer.setMedia(QMediaContent(), inputBuffer);
    mediaplayer.setVolume(100);
    mediaplayer.play();
}

void DataSource::playOutput()
{
    outputBuffer->setBuffer(&outputFile);
    if (!outputBuffer->isOpen())
        outputBuffer->open(QIODevice::ReadOnly);
    mediaplayer.setMedia(QMediaContent(), outputBuffer);
    mediaplayer.setVolume(100);
    mediaplayer.play();
}

void DataSource::readWAV(QString path)
{
    inputData.clear();
    outputData.clear();

    QFile wav;
    wav.setFileName(path);
    wav.open(QIODevice::ReadOnly);
    inputFile = wav.readAll();
    outputFile = QByteArray(inputFile);

    inputAudioFormat->setChannelCount(qFromLittleEndian<quint16>(inputFile.mid(22, 2).data()));
    outputAudioFormat->setChannelCount(qFromLittleEndian<quint16>(outputFile.mid(22, 2).data()));
    qDebug() << "Channels: " << inputAudioFormat->channelCount();

    inputAudioFormat->setSampleRate(qFromLittleEndian<quint32>(inputFile.mid(24, 4).data()));
    outputAudioFormat->setSampleRate(qFromLittleEndian<quint32>(outputFile.mid(24, 4).data()));
    qDebug() << "Sample rate: " << inputAudioFormat->sampleRate();

    inputAudioFormat->setSampleSize(qFromLittleEndian<quint16>(inputFile.mid(34, 2).data()));
    outputAudioFormat->setSampleSize(qFromLittleEndian<quint16>(outputFile.mid(34, 2).data()));
    qDebug() << "Sample size: " << inputAudioFormat->sampleSize() << " b";

    inputDataSize = qFromLittleEndian<quint32>(inputFile.mid(40, 4).data());
    outputDataSize = qFromLittleEndian<quint32>(outputFile.mid(40, 4).data());
    qDebug() << "Data size: " << inputDataSize << " B";

    inputDurationInSeconds = inputDataSize / 2.0 / inputAudioFormat->sampleRate();
    outputDurationInSeconds = outputDataSize / 2.0 / outputAudioFormat->sampleRate();

    for (int i = 0; i < inputDataSize; i += 2) {
        inputData << (qFromLittleEndian<qint16>(inputFile.mid(44 + i, 2).data()));
        outputData << (qFromLittleEndian<qint16>(outputFile.mid(44 + i, 2).data()));
    }
}

void DataSource::increaseInputMagnitude()
{
    inputMagnitude++;
    updateInputChart();
}

void DataSource::increaseOutputMagnitude()
{
    outputMagnitude++;
    updateOutputChart();
}

void DataSource::decreaseInputMagnitude()
{
    if (inputMagnitude > 1) {
        inputMagnitude--;
        if (inputMagnitude == 1)
            inputOffset = 0;
        else
            inputOffsetChanged(inputOffset);
        updateInputChart();
    }
}

void DataSource::decreaseOutputMagnitude()
{
    if (outputMagnitude > 1) {
        outputMagnitude--;
        if (outputMagnitude == 1)
            outputOffset = 0;
        else
            outputOffsetChanged(outputOffset);
        updateOutputChart();
    }
}

void DataSource::inputOffsetChanged(double value)
{
    if (inputMagnitude > 1) {
        inputOffset = value * (inputMagnitude - 1);
        updateInputChart();
    }
}

void DataSource::outputOffsetChanged(double value)
{
    if (outputMagnitude > 1) {
        outputOffset = value * (outputMagnitude - 1);
        updateOutputChart();
    }
}
