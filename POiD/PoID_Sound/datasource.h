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

    Q_INVOKABLE void increaseOutputMagnitude();
    Q_INVOKABLE void decreaseOutputMagnitude();
    Q_INVOKABLE void outputOffsetChanged(double value);
    Q_INVOKABLE void playOutput();



signals:
    void dataLoaded();

private:
    void readWAV(QString path);
    void updateInputChart();
    void updateOutputChart();

    QValueAxis *inputXAxis;
    QValueAxis *inputYAxis;
    QLineSeries *inputSeries;
    QVector<qint16> inputData;
    QAudioFormat *inputAudioFormat;
    double inputDurationInSeconds;
    int inputMagnitude;
    double inputOffset;
    int inputDataSize;

    QValueAxis *outputXAxis;
    QValueAxis *outputYAxis;
    QLineSeries *outputSeries;
    QVector<qint16> outputData;
    QAudioFormat *outputAudioFormat;
    double outputDurationInSeconds;
    int outputMagnitude;
    double outputOffset;
    int outputDataSize;

    QByteArray inputFile;
    QBuffer *inputBuffer;

    QByteArray outputFile;
    QBuffer *outputBuffer;

    QMediaPlayer mediaplayer;
};

#endif // DATASOURCE_H
