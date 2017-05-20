#define _USE_MATH_DEFINES
#include "player.h"
#include <cmath>
#include <QtEndian>
#include <QDebug>

Player::Player(QObject *parent) : QObject(parent)
{
    soundBuffer = new QBuffer(&mediaplayer);
}

// array.replace(44, 2, QByteArray::fromRawData(new char[2] {test & 0xFF, test >> 8}, 2));
// outputFile.replace(24, 4, QByteArray::fromRawData((const char*)&test, 4));

void Player::setAudioHeader(DataSource *data)
{
    soundHeaderByteArray = data->getSoundFileHeader();
}

void Player::setData(DataSource *data)
{
    soundByteArray.clear();

    soundByteArray.append(soundHeaderByteArray);

    QVector<qint16> soundData = data->getSoundData();

    for (int i = 0; i < soundData.size(); i++) {
        qint16 value = soundData.at(i);
        soundByteArray.append(QByteArray::fromRawData((const char*)&value, 2));
    }

    qint32 dataSize = soundData.size() * 2;
    soundByteArray.replace(40, 4, QByteArray::fromRawData((const char*)&dataSize, 4));
}

void Player::play()
{
    soundBuffer->setBuffer(&soundByteArray);
    if (!soundBuffer->isOpen())
        soundBuffer->open(QIODevice::ReadOnly);
    mediaplayer.setMedia(QMediaContent(), soundBuffer);
    mediaplayer.setVolume(100);
    mediaplayer.play();
}

void Player::generateSoundSample(QString frequencyString, QString amplitudeString)
{

    double frequency = frequencyString.toDouble();
    double amplitude = amplitudeString.toDouble();

    double smpleRate = qFromLittleEndian<quint32>(soundHeaderByteArray.mid(24, 4).data());

    generatedSound.clear();
    soundByteArray.clear();
    soundByteArray.append(soundHeaderByteArray);

    for (int i = 0; i < smpleRate; i++) {
        qint16 value = amplitude * std::sin(2 * M_PI * frequency * (i / smpleRate));
        generatedSound.append(value);
        soundByteArray.append(QByteArray::fromRawData((const char*)&value, 2));
    }

    qint32 dataSize = smpleRate * 2;
    soundByteArray.replace(40, 4, QByteArray::fromRawData((const char*)&dataSize, 4));

    generatedMagnitude = 1;
    generatedOffset = 0;
    updateGeneratedChart();
}

void Player::generateSoundMultiSample(Fourier *fourier)
{
    generatedMagnitude = 1;
    generatedOffset = 0;


    qDebug() << "Generation multi tone sound";
    double sampleRate = qFromLittleEndian<quint32>(soundHeaderByteArray.mid(24, 4).data());
    generatedSound.clear();

    soundByteArray.clear();
    soundByteArray.append(soundHeaderByteArray);

    std::vector<double> sequenceFrequency = fourier->getSequenceFrequency();
    std::vector<double> sequenceAmplitude = fourier->getSequenceAmplitude();

    double windowSize = fourier->windowSizeMulti;
    double dataSize = sequenceFrequency.at(sequenceFrequency.size() - 1);

    int amplitudeAccuracy = 16;
    int amplitudeWidth = windowSize / amplitudeAccuracy;

    for (int i = 0; i < sequenceFrequency.size(); i += 3) {
        int sequanceStart = sequenceFrequency.at(i);
        int sequanceStop = sequenceFrequency.at(i + 2);
        int sequanceFreq = sequenceFrequency.at(i + 1);

        for (int j = sequanceStart; j < sequanceStop; j++) {
            double time = j / sampleRate;
            //qDebug() << j << " - " << j / amplitudeWidth << " / " << sequenceAmplitude.size();
            qint16 value = sequenceAmplitude.at(j / amplitudeWidth) * std::sin(2 * M_PI * sequanceFreq * time);
            generatedSound.push_back(value);
        }
    }

    for (int i = 0; i < generatedSound.size(); i++) {
        qint16 value = generatedSound.at(i);
        soundByteArray.append(QByteArray::fromRawData((const char*)&value, 2));
    }

    updateGeneratedChart();
}

void Player::initGeneratedChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis)
{
    generatedSeries = lineSeries;
    generatedXAxis = xAxis;
    generatedYAxis = yAxis;
}

void Player::updateGeneratedChart()
{
    qDebug() << "Updating generated chart";
    generatedSeries->clear();
    double sampleRate = qFromLittleEndian<quint32>(soundHeaderByteArray.mid(24, 4).data());
    double generatedDurationInSeconds = generatedSound.size() / sampleRate;

    double minY = 0;
    double maxY = 0;
    double minX = generatedOffset * generatedDurationInSeconds * (1.0 / generatedMagnitude);
    double maxX = minX + generatedDurationInSeconds * (1.0 / generatedMagnitude);

    int offset;
    int samples = generatedSound.size() * (1.0 / generatedMagnitude);

    if (samples > 400) {
        offset = samples / 400;
    } else {
        offset = 1;
    }

    for (int i = minX * sampleRate; i < maxX * sampleRate; i += offset) {
        generatedSeries->append(i / sampleRate, generatedSound.at(i));
        if (generatedSound.at(i) > maxY)
            maxY = generatedSound.at(i);
        else if (generatedSound.at(i) < minY)
            minY = generatedSound.at(i);
    }

    generatedXAxis->setMin(minX);
    generatedXAxis->setMax(maxX);

    if (abs(maxY) > abs(minY)) {
        generatedYAxis->setMin(-abs(maxY * 1.2));
        generatedYAxis->setMax(abs(maxY * 1.2));
    } else {
        generatedYAxis->setMin(-abs(minY * 1.2));
        generatedYAxis->setMax(abs(minY * 1.2));
    }
}

void Player::increaseGeneratedMagnitude()
{
    generatedMagnitude++;
    updateGeneratedChart();
}

void Player::decreaseGeneratedMagnitude()
{
    if (generatedMagnitude > 1) {
        generatedMagnitude--;
        if (generatedMagnitude == 1)
            generatedOffset = 0;
        else
            generatedOffsetChanged(generatedOffset);
        updateGeneratedChart();
    }
}

void Player::generatedOffsetChanged(double value)
{
    if (generatedMagnitude > 1) {
        generatedOffset = value * (generatedMagnitude - 1);
        updateGeneratedChart();
    }
}
