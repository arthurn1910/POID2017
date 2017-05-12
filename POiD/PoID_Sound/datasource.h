#ifndef DATASOURCE_H
#define DATASOURCE_H

#include <QtCore/QObject>
#include <QtCharts/QAbstractSeries>
#include <QtCharts/QLineSeries>
#include <QtCharts/QValueAxis>
#include <QString>
#include <QAudioFormat>
#include <QMediaPlayer>
#include <QBuffer>
#include <QByteArray>
#include <QAudioOutput>

class QQuickView;

QT_CHARTS_USE_NAMESPACE

class DataSource : public QObject
{
    Q_OBJECT
public:
    explicit DataSource(QObject *parent = 0);

    Q_INVOKABLE void loadSoundData(QString path);
    Q_INVOKABLE void initInputChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis);
    Q_INVOKABLE void initOutputChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis);

    Q_INVOKABLE void increaseInputMagnitude();
    Q_INVOKABLE void decreaseInputMagnitude();
    Q_INVOKABLE void inputOffsetChanged(double value);
    Q_INVOKABLE void playInput();



signals:
    void dataLoaded();

private:
    void readWAV(QString path);
    void updateInputChart();

    QValueAxis *inputXAxis;
    QValueAxis *inputYAxis;
    QLineSeries *inputSeries;
    QVector<qint16> inputData;
    QAudioFormat *inputAudioFormat;
    double inputDurationInSeconds;
    int inputMagnitude;
    double inputOffset;

    QValueAxis *outputXAxis;
    QValueAxis *outputYAxis;
    QLineSeries *outputSeries;

    QByteArray array;
    QBuffer *inputBuffer;
    QMediaPlayer mediaplayer;
    QAudioOutput *audioOutput;
};

#endif // DATASOURCE_H
