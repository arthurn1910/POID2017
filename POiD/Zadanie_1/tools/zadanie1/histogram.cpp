#include "histogram.h"
#include "ui_histogram.h"

#include <QErrorMessage>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtCharts/QChartView>
#include <QtCharts/QBarSeries>
#include <QtCharts/QBarSet>
#include <QtCharts/QLegend>
#include <QtCharts/QBarCategoryAxis>
#include <QtCharts/QValueAxis>
#include <QtCharts/QBarSeries>

Histogram::Histogram(QWidget *parent) :
    Tool(parent),
    ui(new Ui::Histogram)
{
    ui->setupUi(this);
}

Histogram::Histogram(int width, int height, int depth, QWidget *parent) :
    Tool(width, height, depth, parent),
    ui(new Ui::Histogram)
{
    ui->setupUi(this);
}

Histogram::~Histogram()
{
    delete oldHistogramChartWindow;
    delete newHistogramChartWindow;
    delete ui;
}

#include <cmath>
#include <iostream>
QImage *Histogram::process(QImage *image)
{
    QImage *newImage = new QImage(*image);
    int oldHistogramTab[256] {0};
    int newHistogramTab[256] {0};
    if (DEPTH == 8) {
        double alpha = ui->alphaValue->value();
        const uchar *photoData = image->bits();

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                oldHistogramTab[photoData[i * HEIGHT + j]] += 1;
            }
        }

        double colors[256] {0};
//        double min = 256;
//        double max = 0;
        for (int i = 0; i < 256; i++) {
            double suma = 0;
            for (int j = 0; j <= i; j++) {
                suma += oldHistogramTab[j];
            }
            if (suma == 0) {
                suma = 1;
            }

            colors[i] = std::sqrt(2 * alpha * alpha * (std::log((WIDTH * HEIGHT) / suma)));
//            if (colors[i] > max) {
//                max = colors[i];
//            }
//            if (colors[i] < min) {
//                min = colors[i];
//            }
            if (colors[i] > 255) {
                colors[i] = 255;
            }
        }
//        if (max > 255 || min < 0) {
//            std::cout << "tak " << max << " " << min << "\n";
//            for (int i = 0; i < 256; i++) {
//                colors[i] = (colors[i] - min) * 255.0 / (max - min);
//            }
//        }

        for (int i = 0; i < 256; i++) {
            newImage->setColor(i, qRgb(colors[i], colors[i], colors[i]));
        }

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                newHistogramTab[qGray(newImage->color(photoData[i * HEIGHT + j]))] += 1;
            }
        }

        showOldHistogramChart(oldHistogramTab);
        showNewHistogramChart(newHistogramTab);
    } else {        
        QErrorMessage *noForColorImages = new QErrorMessage();
        noForColorImages->showMessage("Tylko dla obrazów w odcieniach szarości!");
    }

    return newImage;
}

QT_CHARTS_USE_NAMESPACE
void Histogram::showOldHistogramChart(int *histogramTab)
{
    QBarSet *histogramSet = new QBarSet("Jasność pixeli");

    for (int i = 0; i < 256; i++) {
        *histogramSet << histogramTab[i];
    }

    QBarSeries *series = new QBarSeries();
    series->append(histogramSet);

    QChart *chart = new QChart();
    chart->addSeries(series);
    chart->setTitle("OLD Histogram");
    chart->setAnimationOptions(QChart::SeriesAnimations);

    chart->createDefaultAxes();
    QValueAxis *axisX = new QValueAxis();
    chart->setAxisX(axisX, series);
    axisX->applyNiceNumbers();
    axisX->setMin(0);
    axisX->setMax(256);

    chart->legend()->setVisible(true);
    chart->legend()->setAlignment(Qt::AlignBottom);

    oldChartView = new QChartView(chart);
    oldChartView->setRenderHint(QPainter::Antialiasing);

    if (oldHistogramChartWindow == nullptr) {
        oldHistogramChartWindow = new QMainWindow();
        oldHistogramChartWindow->resize(1000, 700);
        oldHistogramChartWindow->show();
    }
    oldHistogramChartWindow->setCentralWidget(oldChartView);
}

void Histogram::showNewHistogramChart(int *histogramTab)
{
    QBarSet *histogramSet = new QBarSet("Histogram");

    for (int i = 0; i < 256; i++) {
        *histogramSet << histogramTab[i];
    }

    QBarSeries *series = new QBarSeries();
    series->append(histogramSet);

    QChart *chart = new QChart();
    chart->addSeries(series);
    chart->setTitle("NEW Histogram");
    chart->setAnimationOptions(QChart::SeriesAnimations);

    chart->createDefaultAxes();
    QValueAxis *axisX = new QValueAxis();
    chart->setAxisX(axisX, series);
    axisX->applyNiceNumbers();
    axisX->setMin(0);
    axisX->setMax(256);

    chart->legend()->setVisible(true);
    chart->legend()->setAlignment(Qt::AlignBottom);

    newChartView = new QChartView(chart);
    newChartView->setRenderHint(QPainter::Antialiasing);

    if (newHistogramChartWindow == nullptr) {
        newHistogramChartWindow = new QMainWindow();
        newHistogramChartWindow->resize(1000, 700);
        newHistogramChartWindow->show();
    }
    newHistogramChartWindow->setCentralWidget(newChartView);
}

