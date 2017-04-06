#ifndef EdgeDetectionFilter_H
#define EdgeDetectionFilter_H

#include "../tool_fourier.h"
#include <QWidget>
#include <QSlider>

namespace Ui {
class EdgeDetectionFilter;
}

class EdgeDetectionFilter :  public ToolFourier
{
    Q_OBJECT
public:
    explicit EdgeDetectionFilter(QWidget *parent = 0);
    explicit EdgeDetectionFilter(int width, int height, int depth, QWidget *parent = 0);
    ~EdgeDetectionFilter();

    QImage *process(QImage *image);
    void createMask(std::complex<double> *maskFFT);


private:
    int getEdgeDetectionFilterLevel();
    void updateLookUpTable();
    QSlider *slider;
    Ui::EdgeDetectionFilter *ui;
};

#endif // EdgeDetectionFilter_H
