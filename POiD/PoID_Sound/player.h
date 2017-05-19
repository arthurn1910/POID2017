#ifndef PLAYER_H
#define PLAYER_H

#include <QObject>
#include <QByteArray>
#include <QMediaPlayer>
#include <QBuffer>
#include <vector>

#include "datasource.h"

class Player : public QObject
{
    Q_OBJECT
public:
    explicit Player(QObject *parent = 0);

    Q_INVOKABLE void setAudioHeader(DataSource *data);
    Q_INVOKABLE void setData(DataSource *data);
    Q_INVOKABLE void generateSoundSample(QString frequencyString, QString amplitudeString);

    Q_INVOKABLE void play();

signals:

public slots:
private:

    QBuffer *soundBuffer;

    QByteArray soundByteArray;
    QByteArray soundHeaderByteArray;
    QMediaPlayer mediaplayer;
};

#endif // PLAYER_H
