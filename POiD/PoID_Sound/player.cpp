#define _USE_MATH_DEFINES
#include "player.h"
#include <cmath>
#include <QtEndian>

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
    QVector<qint16> soundData;

    soundByteArray.clear();
    soundByteArray.append(soundHeaderByteArray);

    for (int i = 0; i < smpleRate; i++) {
        qint16 value = amplitude * std::sin(2 * M_PI * frequency * (i / smpleRate));
        soundByteArray.append(QByteArray::fromRawData((const char*)&value, 2));
    }

    qint32 dataSize = smpleRate * 2;
    soundByteArray.replace(40, 4, QByteArray::fromRawData((const char*)&dataSize, 4));
}
