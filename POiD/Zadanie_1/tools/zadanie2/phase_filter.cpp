#define _USE_MATH_DEFINES
#include "phase_filter.h"
#include "ui_phase_filter.h"

#include <cmath>
#include <QDebug>

PhaseFilter::PhaseFilter(QWidget *parent) :
    ToolFourier(parent),
    ui(new Ui::PhaseFilter)
{
    ui->setupUi(this);
}

PhaseFilter::PhaseFilter(int width, int height, int depth, QWidget *parent) :
    ToolFourier(width, height, depth, parent),
    ui(new Ui::PhaseFilter)
{
    ui->setupUi(this);
}

PhaseFilter::~PhaseFilter()
{
    delete ui;
}


QImage *PhaseFilter::process(QImage *image)
{
    ui->amplitudeButton->setEnabled(true);
    ui->phaseButton->setEnabled(true);
    return ToolFourier::process(image);
}

void PhaseFilter::createMask(std::complex<double> *maskFFT)
{
    double parametrK = ui->kSpinBox->value();
    double parametrL = ui->lSpinBox->value();

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            std::complex<double> complex;
            complex.real(0);
            complex.imag((-(i * parametrK * 2 * M_PI) / HEIGHT) + (-(j * parametrL * 2 * M_PI) / WIDTH) + (parametrK + parametrL) * M_PI);
            maskFFT[i * WIDTH + j] = std::exp(complex);
            if (j == 0)
                qDebug() << "Arg: " << std::arg(maskFFT[i * WIDTH + j]);
        }
    }
}


