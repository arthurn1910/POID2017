#define _USE_MATH_DEFINES
#include "edge_detection_filter.h"
#include "ui_edge_detection_filter.h"

#include <cmath>

EdgeDetectionFilter::EdgeDetectionFilter(QWidget *parent) :
    ToolFourier(parent),
    ui(new Ui::EdgeDetectionFilter)
{
    ui->setupUi(this);
}

EdgeDetectionFilter::EdgeDetectionFilter(int width, int height, int depth, QWidget *parent) :
    ToolFourier(width, height, depth, parent),
    ui(new Ui::EdgeDetectionFilter)
{
    ui->setupUi(this);
}

EdgeDetectionFilter::~EdgeDetectionFilter()
{
    delete ui;
}


QImage *EdgeDetectionFilter::process(QImage *image)
{
    ui->amplitudeButton->setEnabled(true);
    ui->phaseButton->setEnabled(true);
    return ToolFourier::process(image);
}

void EdgeDetectionFilter::createMask(std::complex<double> *maskFFT) {
    double lineAngle = -ui->lineAngleDoubleSpinBox->value() * M_PI / 180;
    double openingAngle = -(ui->openingAngle->value() / 2) * M_PI / 180;
    double radius = ui->radius->value();

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            if (i == HEIGHT/2 && j == WIDTH/2) {
                maskFFT[i * HEIGHT + j].real(1);
            } else if (std::pow(WIDTH/2 - j, 2) + std::pow(HEIGHT/2 - i, 2) <= radius * radius) {
                maskFFT[i * HEIGHT + j].real(0);
            }
            else if ((lineAngle + openingAngle > (M_PI / 2.0) && lineAngle - openingAngle < (M_PI / 2.0)) ||
               ((lineAngle + openingAngle < (M_PI / -2.0) && lineAngle - openingAngle > (M_PI / -2.0)))) {
                if (i < HEIGHT/2) {
                    if (std::tan(lineAngle + openingAngle) * (j - WIDTH/2) + HEIGHT/2 - i >= 0  &&
                            std::tan(lineAngle - openingAngle) * (j - WIDTH/2) + HEIGHT/2 - i >= 0) {
                        maskFFT[i * HEIGHT + j].real(1);
                    } else {
                        maskFFT[i * HEIGHT + j].real(0);
                    }
                } else {
                    if (std::tan(lineAngle + openingAngle) * (j - WIDTH/2) + HEIGHT/2 - i <= 0  &&
                            std::tan(lineAngle - openingAngle) * (j - WIDTH/2) + HEIGHT/2 - i <= 0) {
                        maskFFT[i * HEIGHT + j].real(1);
                    } else {
                        maskFFT[i * HEIGHT + j].real(0);
                    }
                }
            } else {
                if (j < WIDTH/2) {
                    if (std::tan(lineAngle + openingAngle) * (j - WIDTH/2) + HEIGHT/2 - i >= 0 &&
                            std::tan(lineAngle - openingAngle) * (j - WIDTH/2) + HEIGHT/2 - i <= 0) {
                        maskFFT[i * HEIGHT + j].real(1);
                    } else {
                        maskFFT[i * HEIGHT + j].real(0);
                    }
                } else {
                    if (std::tan(lineAngle + openingAngle) * (j - WIDTH/2) + HEIGHT/2 - i <= 0 &&
                            std::tan(lineAngle - openingAngle) * (j - WIDTH/2) + HEIGHT/2 - i >= 0) {
                        maskFFT[i * HEIGHT + j].real(1);
                    } else {
                        maskFFT[i * HEIGHT + j].real(0);
                    }
                }
            }
        }
    }
}


