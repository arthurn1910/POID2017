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


// array.replace(44, 2, QByteArray::fromRawData(new char[2] {test & 0xFF, test >> 8}, 2));
// outputFile.replace(24, 4, QByteArray::fromRawData((const char*)&test, 4));

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

void DataSource::initAmdfChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis)
{
    amdfSeries = lineSeries;
    amdfXAxis = xAxis;
    amdfYAxis = yAxis;
}

void DataSource::loadSoundData(QString path)
{
    inputMagnitude = 1;
    inputOffset = 0;

    amdfMagnitude = 1;
    amdfOffset = 0;

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

void DataSource::updateAmdfChart()
{
    qDebug() << "Updating amdf chart";
    amdfSeries->clear();
    double sampleRate = inputAudioFormat->sampleRate();

    double minY = 0;
    double maxY = 0;
    double minX = amdfOffset * amdfDurationInSeconds * (1.0 / amdfMagnitude);
    double maxX = minX + amdfDurationInSeconds * (1.0 / amdfMagnitude);

    int offset;
    int samples = sampleRate * (1.0 / amdfMagnitude);

    if (samples > 200) {
        offset = samples / 200;
    } else {
        offset = 1;
    }

    qDebug() << "Offset: " << offset;

    for (int i = minX * sampleRate; i < maxX * sampleRate; i += offset) {

        qDebug() << "I: " << i << ", " << amdfData.at(i);
        amdfSeries->append(i / sampleRate, amdfData.at(i));
        if (amdfData.at(i) > maxY)
            maxY = amdfData.at(i);
        if (amdfData.at(i) < minY)
            minY = amdfData.at(i);
    }

    amdfXAxis->setMin(minX);
    amdfXAxis->setMax(maxX);

    amdfYAxis->setMin(minY - 10);
    amdfYAxis->setMax(maxY * 1.1);
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

void DataSource::readWAV(QString path)
{
    inputData.clear();
    amdfData.clear();

    QFile wav;
    wav.setFileName(path);
    wav.open(QIODevice::ReadOnly);
    inputFile = wav.readAll();
    amdfByteArray = QByteArray(inputFile.mid(0, 44));

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

void DataSource::increaseAmdfMagnitude()
{
    amdfMagnitude++;
    updateAmdfChart();
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

void DataSource::decreaseAmdfMagnitude()
{
    if (amdfMagnitude > 1) {
        amdfMagnitude--;
        if (amdfMagnitude == 1)
            amdfOffset = 0;
        else
            amdfOffsetChanged(amdfOffset);
        updateAmdfChart();
    }
}

void DataSource::inputOffsetChanged(double value)
{
    if (inputMagnitude > 1) {
        inputOffset = value * (inputMagnitude - 1);
        updateInputChart();
    }
}

void DataSource::amdfOffsetChanged(double value)
{
    if (amdfMagnitude > 1) {
        amdfOffset = value * (amdfMagnitude - 1);
        updateAmdfChart();
    }
}


void DataSource::runAMDF()
{
    amdfData.clear();
    for (int offset = 0; offset < inputData.size(); offset++) {
        double amdf = 0;
        for (int i = 0; i < inputData.size(); i++) {
            //qDebug() << "I: " << i << ", with offset: " << (i + offset) % inputData.size();
            amdf += std::abs(inputData.at(i) - inputData.at((i + offset) % inputData.size()));
        }
        amdf /= 1000;
        amdfData.append(amdf);
    }
    amdfDurationInSeconds = amdfData.size() / 2.0 / inputAudioFormat->sampleRate();

    updateAmdfChart();
}
