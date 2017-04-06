#ifndef LowHighPassFilter_H
#define LowHighPassFilter_H

#include "../tool_fourier.h"
#include <QWidget>
#include <QSlider>

namespace Ui {
class LowHighPassFilter;
}

class LowHighPassFilter :  public ToolFourier
{
    Q_OBJECT
public:
    explicit LowHighPassFilter(QWidget *parent = 0);
    explicit LowHighPassFilter(int width, int height, int depth, QWidget *parent = 0);
    ~LowHighPassFilter();

    QImage *process(QImage *image);
    void createMask(std::complex<double> *maskFFT);


private:
    int getLowHighPassFilterLevel();
    void updateLookUpTable();
    QSlider *slider;
    Ui::LowHighPassFilter *ui;
};

#endif // LowHighPassFilter_H
