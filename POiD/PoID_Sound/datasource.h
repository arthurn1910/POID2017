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
#include <vector>

class QQuickView;

QT_CHARTS_USE_NAMESPACE

class DataSource : public QObject
{
    Q_OBJECT
public:
    explicit DataSource(QObject *parent = 0);

    Q_INVOKABLE void loadSoundData(QString path);
    Q_INVOKABLE void loadSoundHeader(QString path);
    Q_INVOKABLE void initInputChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis);

    Q_INVOKABLE void increaseInputMagnitude();
    Q_INVOKABLE void decreaseInputMagnitude();
    Q_INVOKABLE void inputOffsetChanged(double value);

    QVector<qint16> getInputData();
    int getSampleRate();

    QByteArray getSoundFileHeader();
    QVector<qint16> getSoundData();

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
    qint32 inputDataSize;

    QByteArray inputFile;
    QByteArray soundHeader;
};

#endif // DATASOURCE_H
