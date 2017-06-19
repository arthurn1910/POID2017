#ifndef FILTER_H
#define FILTER_H

#include <QObject>
#include <QtCharts/QAbstractSeries>
#include <QtCharts/QLineSeries>
#include <QtCharts/QValueAxis>
#include <vector>
#include "datasource.h"
#include <complex>

class Filter : public QObject
{
    Q_OBJECT
public:
    explicit Filter(QObject *parent = 0);
    Q_PROPERTY(QString amplitude READ getAmplitude)
    Q_PROPERTY(QString windowParam WRITE setWindowParam)
    Q_PROPERTY(QString windowMParam WRITE setWindowMParam)
    Q_PROPERTY(QString hopsizeParam WRITE setHopsizeParam)
    Q_PROPERTY(QString fcParam WRITE setFcParam)
    Q_PROPERTY(QString filterLengthParam WRITE setLengthParam)

    Q_INVOKABLE void setData(DataSource *data);
    Q_INVOKABLE void initFilterChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis);

    Q_INVOKABLE void setWindowType(QString type);

    Q_INVOKABLE void increaseFilterMagnitude();
    Q_INVOKABLE void decreaseFilterMagnitude();
    Q_INVOKABLE void filterOffsetChanged(double value);
    Q_INVOKABLE void runFilter();
    Q_INVOKABLE void runFilterSplot();
    Q_INVOKABLE void runEqualizer(double one, double two, double three, double four, double five);


    void setWindowParam(QString value);
    void setWindowMParam(QString value);
    void setHopsizeParam(QString value);
    void setFcParam(QString value);
    void setLengthParam(QString value);

    QString getAmplitude();
    QVector<qint16> getSoundData();

signals:
    void filterFinished();

private:
    void FFT(std::complex<float> *input, const int size);
    void IFFT(std::complex<float> *input, const int size);
    void updateFilterChart();

    double findMostSimilar(std::vector<float> vec, int windowSize);

    QList<double> createFilter();
    double windowValue(int iter, int windowSize);

    QVector<qint16> inputData;
    QVector<qint16> outputData;

    QValueAxis *filterXAxis;
    QValueAxis *filterYAxis;
    QLineSeries *filterSeries;
    std::vector<float> filterData;
    double filterDurationInSeconds;
    int filterMagnitude;
    double filterOffset;
    double filterMinimum;
    double filterMedian;
    double filterOptimalization;
    double amplitude;
    int windowType;

    int windowN;
    int windowM;
    int hopSizeR;
    int fc;
    int filterL;
};

#endif // FILTER_H
