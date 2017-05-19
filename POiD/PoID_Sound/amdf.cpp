#include "amdf.h"
#include "persistence1d/persistence1d.hpp"

Amdf::Amdf(QObject *parent) : QObject(parent)
{
    amdfOptimalization = 10;
}

void Amdf::initAmdfChart(QLineSeries *lineSeries, QValueAxis *xAxis, QValueAxis *yAxis)
{
    amdfSeries = lineSeries;
    amdfXAxis = xAxis;
    amdfYAxis = yAxis;
}

void Amdf::setData(DataSource *data)
{
    amdfMagnitude = 1;
    amdfOffset = 0;

    inputData = data->getInputData();
}

void Amdf::updateAmdfChart()
{
    qDebug() << "Updating amdf chart";
    amdfSeries->clear();
    double dataSize = inputData.size();

    double maxY = 0;
    double minX = amdfOffset * amdfDurationInSeconds * (1.0 / amdfMagnitude);
    double maxX = minX + amdfDurationInSeconds * (1.0 / amdfMagnitude);

    int offset;
    int samples = dataSize * (1.0 / amdfMagnitude);

    if (samples > 300) {
        offset = samples / 300;
    } else {
        offset = 1;
    }


    for (int i = minX * dataSize; i < maxX * dataSize; i += offset) {
        amdfSeries->append(i / dataSize, amdfData.at(i));
        if (amdfData.at(i) > maxY)
            maxY = amdfData.at(i);
    }

    amdfXAxis->setMin(minX);
    amdfXAxis->setMax(maxX);

    amdfYAxis->setMin(-10);
    amdfYAxis->setMax(maxY * 1.1);
}

void Amdf::increaseAmdfMagnitude()
{
    amdfMagnitude++;
    updateAmdfChart();
}
void Amdf::decreaseAmdfMagnitude()
{
    if (amdfMagnitude > 1) {
        amdfMagnitude--;
        if (amdfMagnitude == 1)
            amdfOffset = 0;
        else
            amdfOffsetChanged(amdfOffset);
        updateAmdfChart();
    }
}
void Amdf::amdfOffsetChanged(double value)
{
    if (amdfMagnitude > 1) {
        amdfOffset = value * (amdfMagnitude - 1);
        updateAmdfChart();
    }
}

void Amdf::runAMDF()
{
    amplitude = 0;
    amdfData.clear();
    for (int offset = 0; offset < inputData.size() / amdfOptimalization; offset++) {
        double amdf = 0;
        for (int i = 0; i < inputData.size(); i++) {
            if (amplitude < std::fabs(inputData.at(i)))
                amplitude = std::fabs(inputData.at(i));
            amdf += std::abs(inputData.at(i) - inputData.at((i + offset) % inputData.size()));
        }
        //qDebug() << "I: " << offset;
        amdf /= 1000;
        amdfData.push_back(amdf);
    }
    amdfDurationInSeconds = amdfData.size() / (double) inputData.size();

    p1d::Persistence1D p;
    p.RunPersistence(amdfData);
    std::vector< p1d::TPairedExtrema > Extrema;
    p.GetPairedExtrema(Extrema, 10);

    QVector<int> minVector;

    for(std::vector< p1d::TPairedExtrema >::iterator it = Extrema.begin(); it != Extrema.end(); it++)
    {
        minVector.append((*it).MinIndex);
    }

    qSort(minVector);

    amdfMinimum = (double) inputData.size() / minVector.first();

    for (int i = minVector.size() - 1; i >= 1; i--) {
        minVector.replace(i, minVector.at(i) - minVector.at(i - 1));
    }
    qSort(minVector);
    amdfMedian = (double) inputData.size() / minVector.at(minVector.size() / 2);

    updateAmdfChart();
    emit amdfFinished();
}


QString Amdf::getAmdfMinimum()
{
    return QString::number(amdfMinimum, 'f', 2);
}

QString Amdf::getAmdfMedian()
{
    return QString::number(amdfMedian, 'f', 2);
}

QString Amdf::getAmdfOptimalization()
{
    return QString::number(amdfOptimalization, 'f', 0);
}

void Amdf::setAmdfOptimalization(QString value)
{
    amdfOptimalization = (int) value.toDouble();
    if (amdfOptimalization < 1)
        amdfOptimalization = 1;
}

QString Amdf::getAmplitude()
{
    return QString::number(amplitude, 'f', 2);
}
