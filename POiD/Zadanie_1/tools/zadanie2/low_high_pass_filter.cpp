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

std::complex<double> *LowHighPassFilter::applayMask(const std::complex<double> *originalFFT, std::complex<double> *maskFFT)
{
    std::complex<double> *withMaskFFT = new std::complex<double>[WIDTH * HEIGHT] {0};
    double radius = ui->radiusDoubleSpinBox->value();
    if (ui->lowPassRadioButton->isChecked()) {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (std::pow(WIDTH/2 - j, 2) + std::pow(HEIGHT/2 - i, 2) > radius * radius) {
                    maskFFT[i * HEIGHT + j].real(1);
                } else {
                    maskFFT[i * HEIGHT + j].real(0);
                }
            }
        }
    } else {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (std::pow(WIDTH/2 - j, 2) + std::pow(HEIGHT/2 - i, 2) <= radius * radius) {
                    maskFFT[i * HEIGHT + j].real(1);
                } else {
                    maskFFT[i * HEIGHT + j].real(0);
                }
            }
        }
    }

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            withMaskFFT[i * HEIGHT + j] = maskFFT[i * HEIGHT + j] * originalFFT[i * HEIGHT + j];
        }
    }

    return withMaskFFT;
}
