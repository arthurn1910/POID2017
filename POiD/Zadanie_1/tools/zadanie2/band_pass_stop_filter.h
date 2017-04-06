#ifndef BandPassStopFilter_H
#define BandPassStopFilter_H

#include "../tool_fourier.h"
#include <QWidget>
#include <QSlider>

namespace Ui {
class BandPassStopFilter;
}

class BandPassStopFilter :  public ToolFourier
{
    Q_OBJECT
public:
    explicit BandPassStopFilter(QWidget *parent = 0);
    explicit BandPassStopFilter(int width, int height, int depth, QWidget *parent = 0);
    ~BandPassStopFilter();

    QImage *process(QImage *image);
    void createMask(std::complex<double> *maskFFT);


private:
    int getBandPassStopFilterLevel();
    void updateLookUpTable();
    QSlider *slider;
    Ui::BandPassStopFilter *ui;
};

#endif // BandPassStopFilter_H
