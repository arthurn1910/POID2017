#ifndef FOURIER_H
#define FOURIER_H

#include <QObject>
#include <QtCharts/QAbstractSeries>
#include <QtCharts/QLineSeries>
#include <QtCharts/QValueAxis>
#include <vector>
#include "datasource.h"
#include <complex>

class Fourier : public QObject
{
    Q_OBJECT
public:
    explicit Fourier(QObject *parent = 0);

    Q_PROPERTY(QString threasholdParam READ getThreasholdParam WRITE setThreasholdParam)
    Q_PROPERTY(QString basicFrequency READ getBasicFrequency)
    Q_PROPERTY(QString amplitude READ getAmplitude)
    Q_PROPERTY(QString windowParam WRITE setWindowParam)
    Q_PROPERTY(QString frequencyThreshold WRITE setFrequencyThreshold)

    Q_INVOKABLE void setData(DataSource *data);
    Q_INVOKABLE void initFourierChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis);

    Q_INVOKABLE void increaseFourierMagnitude();
    Q_INVOKABLE void decreaseFourierMagnitude();
    Q_INVOKABLE void fourierOffsetChanged(double value);
    Q_INVOKABLE void runFOURIER();
    Q_INVOKABLE void runFOURIERmultiTone();

    Q_INVOKABLE void setWindowType(QString type);

    void setFrequencyThreshold(QString value);
    void setWindowParam(QString value);
    std::vector<double> getSequenceFrequency();
    std::vector<double> getSequenceAmplitude();

    QString getBasicFrequency();
    QString getAmplitude();
    QString getThreasholdParam();
    void setThreasholdParam(QString value);

    qint32 windowSizeMulti;

signals:
    void fourierFinished();

public slots:
private:
    void FFT(std::complex<float> *input, const int size);
    void IFFT(std::complex<float> *input, const int size);

    double windowGauss(int iter, int all);
    double windowHanningOne(int iter, int all);
    double windowHanningTwo(int iter, int all);
    double windowBarlett(int iter, int all);

    double findMaxMedian(std::vector<float> vec);
    double findMostSimilar(std::vector<float> vec, int windowSize);

    void updateFourierChart();

    QVector<qint16> inputData;
    double sampleRate;

    QValueAxis *fourierXAxis;
    QValueAxis *fourierYAxis;
    QLineSeries *fourierSeries;
    std::vector<float> fourierAmplitudeData;
    std::complex<float> *fourierComplexData;
    double fourierDurationInSeconds;
    int fourierMagnitude;
    double fourierOffset;

    double logThreashold;
    int windowType;

    double mostSimilar;
    double amplitude;

    std::vector<double> sequenceFrequency;
    std::vector<double> sequenceAmplitude;
    double frequencyThreshold;
};

#endif // FOURIER_H
