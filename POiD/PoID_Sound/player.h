#ifndef PLAYER_H
#define PLAYER_H

#include <QObject>
#include <QByteArray>
#include <QMediaPlayer>
#include <QBuffer>
#include <vector>
#include <QtCharts/QAbstractSeries>
#include <QtCharts/QLineSeries>
#include <QtCharts/QValueAxis>

#include "datasource.h"
#include "fourier.h"
#include "filter.h"

class Player : public QObject
{
    Q_OBJECT
public:
    explicit Player(QObject *parent = 0);

    Q_INVOKABLE void setAudioHeader(DataSource *data);
    Q_INVOKABLE void setData(DataSource *data);
    Q_INVOKABLE void generateSoundSample(QString frequencyString, QString amplitudeString);
    Q_INVOKABLE void generateSoundMultiSample(Fourier *fourier);
    Q_INVOKABLE void generateSound(Filter *filter);

    Q_INVOKABLE void initGeneratedChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis);
    Q_INVOKABLE void increaseGeneratedMagnitude();
    Q_INVOKABLE void decreaseGeneratedMagnitude();
    Q_INVOKABLE void generatedOffsetChanged(double value);

    Q_INVOKABLE void play();

private:
    void updateGeneratedChart();

    QValueAxis *generatedXAxis;
    QValueAxis *generatedYAxis;
    QLineSeries *generatedSeries;
    int generatedMagnitude;
    double generatedOffset;

    QVector<qint16> generatedSound;

    QBuffer *soundBuffer;

    QByteArray soundByteArray;
    QByteArray soundHeaderByteArray;
    QMediaPlayer mediaplayer;
};

#endif // PLAYER_H
