#ifndef Histogram_H
#define Histogram_H

#include <QWidget>
#include <QLabel>
#include <QMainWindow>
#include <QtCharts/QChartView>
#include "../tool.h"

QT_CHARTS_USE_NAMESPACE

namespace Ui {
class Histogram;
}

class Histogram : public Tool
{
public:
    explicit Histogram(QWidget *parent = 0);
    explicit Histogram(int width, int height, int depth, QWidget *parent = 0);
    ~Histogram();

    QImage* process(QImage *photoData);
    //void setDefaultParameters();

private:
    //void updateLookUpTable();

    void showOldHistogramChart(int *histogramTab);
    void showNewHistogramChart(int *histogramTab);

    QChartView *oldChartView;
    QMainWindow *oldHistogramChartWindow = nullptr;
    QChartView *newChartView;
    QMainWindow *newHistogramChartWindow = nullptr;
    QLabel* consoleValue;
    Ui::Histogram *ui;
};

#endif // Histogram_H
