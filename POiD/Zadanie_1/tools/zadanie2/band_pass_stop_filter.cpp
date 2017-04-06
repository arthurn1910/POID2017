#include "band_pass_stop_filter.h"
#include "ui_band_pass_stop_filter.h"

#include <QErrorMessage>

BandPassStopFilter::BandPassStopFilter(QWidget *parent) :
    ToolFourier(parent),
    ui(new Ui::BandPassStopFilter)
{
    ui->setupUi(this);
}

BandPassStopFilter::BandPassStopFilter(int width, int height, int depth, QWidget *parent) :
    ToolFourier(width, height, depth, parent),
    ui(new Ui::BandPassStopFilter)
{
    ui->setupUi(this);
}

BandPassStopFilter::~BandPassStopFilter()
{
    delete ui;
}


QImage *BandPassStopFilter::process(QImage *image)
{
    if (ui->interiorRadiusDoubleSpinBox->value() < ui->exteriorRadiusDoubleSpinBox->value())
    {
        QErrorMessage *radiusError = new QErrorMessage();
        radiusError->showMessage("Kąt wewnętrzny nie może być większy niż zewnętrzny.");
        return image;
    }
    ui->amplitudeButton->setEnabled(true);
    ui->phaseButton->setEnabled(true);
    return ToolFourier::process(image);
}


void BandPassStopFilter::createMask(std::complex<double> *maskFFT) {
    double interiorRadius = ui->interiorRadiusDoubleSpinBox->value();
    double exteriorRadius = ui->exteriorRadiusDoubleSpinBox->value();
    if (ui->bandPassRadioButton->isChecked()) {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (std::pow(WIDTH/2 - j, 2) + std::pow(HEIGHT/2 - i, 2) >= std::pow(interiorRadius, 2) &&
                       std::pow(WIDTH/2 - j, 2) + std::pow(HEIGHT/2 - i, 2) <= std::pow(exteriorRadius, 2)) {
                    maskFFT[i * HEIGHT + j].real(1);
                } else {
                    maskFFT[i * HEIGHT + j].real(0);
                }
            }
        }
    } else {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (std::pow(WIDTH/2 - j, 2) + std::pow(HEIGHT/2 - i, 2) < std::pow(interiorRadius, 2) ||
                        std::pow(WIDTH/2 - j, 2) + std::pow(HEIGHT/2 - i, 2) > std::pow(exteriorRadius, 2)) {
                    maskFFT[i * HEIGHT + j].real(1);
                } else {
                    maskFFT[i * HEIGHT + j].real(0);
                }
            }
        }
    }
}
