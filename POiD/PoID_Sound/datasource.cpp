#include "datasource.h"
#include <QtCharts/QXYSeries>
#include <QtCharts/QAreaSeries>
#include <QtQuick/QQuickView>
#include <QtQuick/QQuickItem>
#include <QtCore/QDebug>
#include <QtCore/QtMath>
#include <QtEndian>
#include <QDataStream>
#include <cmath>

QT_CHARTS_USE_NAMESPACE

Q_DECLARE_METATYPE(QAbstractSeries *)
Q_DECLARE_METATYPE(QAbstractAxis *)


DataSource::DataSource(QObject *parent) : QObject(parent)
{
    inputAudioFormat = new QAudioFormat();
}

void DataSource::initInputChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis)
{
    inputSeries = lineSeries;
    inputXAxis = xAxis;
    inputYAxis = yAxis;
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
    qDebug() << "Updating input chart";
    inputSeries->clear();
    double sampleRate = inputAudioFormat->sampleRate();

    double minY = 0;
    double maxY = 0;
    double minX = inputOffset * inputDurationInSeconds * (1.0 / inputMagnitude);
    double maxX = minX + inputDurationInSeconds * (1.0 / inputMagnitude);

    int offset;
    int samples = sampleRate * (1.0 / inputMagnitude);

    if (samples > 300) {
        offset = samples / 300;
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

void DataSource::readWAV(QString path)
{
    inputData.clear();

    QFile wav;
    wav.setFileName(path);
    wav.open(QIODevice::ReadOnly);
    inputFile = wav.readAll();

    inputAudioFormat->setChannelCount(qFromLittleEndian<quint16>(inputFile.mid(22, 2).data()));
    qDebug() << "Channels: " << inputAudioFormat->channelCount();

    inputAudioFormat->setSampleRate(qFromLittleEndian<quint32>(inputFile.mid(24, 4).data()));
    qDebug() << "Sample rate: " << inputAudioFormat->sampleRate();

    inputAudioFormat->setSampleSize(qFromLittleEndian<quint16>(inputFile.mid(34, 2).data()));
    qDebug() << "Sample size: " << inputAudioFormat->sampleSize() << " b";

    inputDataSize = qFromLittleEndian<quint32>(inputFile.mid(40, 4).data());
    qDebug() << "Data size: " << inputDataSize << " B";

    inputDurationInSeconds = inputDataSize / 2.0 / inputAudioFormat->sampleRate();

    for (int i = 0; i < inputDataSize; i += 2) {
        inputData << (qFromLittleEndian<qint16>(inputFile.mid(44 + i, 2).data()));
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

QVector<qint16> DataSource::getInputData() {
    return inputData;
}

int DataSource::getSampleRate()
{
    return inputAudioFormat->sampleRate();
}


QByteArray DataSource::getSoundFileHeader()
{
    return inputFile.mid(0, 44);
}

QVector<qint16> DataSource::getSoundData()
{
    return inputData;
}
