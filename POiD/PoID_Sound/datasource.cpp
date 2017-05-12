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

DataSource::DataSource(QObject *parent) : QObject(parent)
{
    inputAudioFormat = new QAudioFormat();
    inputBuffer = new QBuffer(&mediaplayer);
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

    readWAV(path.mid(8));

    updateInputChart();

    emit dataLoaded();
}

void DataSource::updateInputChart()
{
    inputSeries->clear();
    double sampleRate = inputAudioFormat->sampleRate();

    int minY = 0;
    int maxY = 0;
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

void DataSource::playInput()
{
    inputBuffer->setBuffer(&array);
    if (!inputBuffer->isOpen())
        inputBuffer->open(QIODevice::ReadOnly);
    mediaplayer.setMedia(QMediaContent(), inputBuffer);
    mediaplayer.setVolume(100);
    mediaplayer.play();
}

void DataSource::readWAV(QString path)
{
    inputData.clear();

    QFile wav;
    wav.setFileName(path);
    wav.open(QIODevice::ReadOnly);
    array = wav.readAll();

    inputAudioFormat->setChannelCount(qFromLittleEndian<quint16>(array.mid(22, 2).data()));
    qDebug() << "Channels: " << inputAudioFormat->channelCount();

    inputAudioFormat->setSampleRate(qFromLittleEndian<quint32>(array.mid(24, 4).data()));
    qDebug() << "Sample rate: " << inputAudioFormat->sampleRate();

    inputAudioFormat->setSampleSize(qFromLittleEndian<quint16>(array.mid(34, 2).data()));
    qDebug() << "Sample size: " << inputAudioFormat->sampleSize();

    qint32 dataSize = qFromLittleEndian<quint32>(array.mid(40, 4).data());
    qDebug() << "Data size: " << dataSize;

    inputDurationInSeconds = dataSize / 2.0 / inputAudioFormat->sampleRate();

    for (int i = 0; i < dataSize; i += 2) {
        inputData << (qFromLittleEndian<qint16>(array.mid(44 + i, 2).data()));
    }
}

void DataSource::increaseInputMagnitude()
{
    inputMagnitude++;
    updateInputChart();
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

void DataSource::inputOffsetChanged(double value)
{
    if (inputMagnitude > 1) {
        inputOffset = value * (inputMagnitude - 1);
        updateInputChart();
    }
}
