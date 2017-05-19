#ifndef AMDF_H
#define AMDF_H

#include <QObject>
#include <QtCharts/QAbstractSeries>
#include <QtCharts/QLineSeries>
#include <QtCharts/QValueAxis>
#include <vector>
#include "datasource.h"


QT_CHARTS_USE_NAMESPACE

class Amdf : public QObject
{
    Q_OBJECT
public:
    explicit Amdf(QObject *parent = 0);

    Q_PROPERTY(QString amdfBasicFrequencyMinimum READ getAmdfMinimum)
    Q_PROPERTY(QString amdfBasicFrequencyMedian READ getAmdfMedian)
    Q_PROPERTY(QString amplitude READ getAmplitude)
    Q_PROPERTY(QString amdfOptimalizationParam READ getAmdfOptimalization WRITE setAmdfOptimalization)

    Q_INVOKABLE void setData(DataSource *data);
    Q_INVOKABLE void initAmdfChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis);

    Q_INVOKABLE void increaseAmdfMagnitude();
    Q_INVOKABLE void decreaseAmdfMagnitude();
    Q_INVOKABLE void amdfOffsetChanged(double value);
    Q_INVOKABLE void runAMDF();

    QString getAmdfMinimum();
    QString getAmdfMedian();
    QString getAmplitude();
    QString getAmdfOptimalization();
    void setAmdfOptimalization(QString value);

signals:
    void amdfFinished();

private:
    void updateAmdfChart();

    QVector<qint16> inputData;

    QValueAxis *amdfXAxis;
    QValueAxis *amdfYAxis;
    QLineSeries *amdfSeries;
    std::vector<float> amdfData;
    double amdfDurationInSeconds;
    int amdfMagnitude;
    double amdfOffset;
    double amdfMinimum;
    double amdfMedian;
    double amdfOptimalization;
    double amplitude;
};

#endif // AMDF_H
