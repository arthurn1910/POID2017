#include "low_high_pass_filter.h"
#include "ui_low_high_pass_filter.h"

#include <iostream>

LowHighPassFilter::LowHighPassFilter(QWidget *parent) :
    ToolFourier(parent),
    ui(new Ui::LowHighPassFilter)
{
    ui->setupUi(this);
}

LowHighPassFilter::LowHighPassFilter(int width, int height, int depth, QWidget *parent) :
    ToolFourier(width, height, depth, parent),
    ui(new Ui::LowHighPassFilter)
{
    ui->setupUi(this);
}

LowHighPassFilter::~LowHighPassFilter()
{
    delete ui;
}


QImage *LowHighPassFilter::process(QImage *image)
{
    ui->amplitudeButton->setEnabled(true);
    ui->phaseButton->setEnabled(true);
    return ToolFourier::process(image);
}

void LowHighPassFilter::applayMask(std::complex<double> *originalFFT, std::complex<double> *withMaskFFT)
{
    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        withMaskFFT[i] = originalFFT[i];
    }
}
