#include "low_pass_filter.h"
#include "ui_low_pass_filter.h"

#include <iostream>

LowPassFilter::LowPassFilter(QWidget *parent) :
    ToolFourier(parent),
    ui(new Ui::LowPassFilter)
{
    ui->setupUi(this);
}

LowPassFilter::LowPassFilter(int width, int height, int depth, QWidget *parent) :
    ToolFourier(width, height, depth, parent),
    ui(new Ui::LowPassFilter)
{
    ui->setupUi(this);
}

LowPassFilter::~LowPassFilter()
{
    delete ui;
}

QImage *LowPassFilter::process(QImage *image)
{
    QImage *newImage;
    Complex *test = new Complex[WIDTH * HEIGHT];
    const uchar *photoData = image->bits();
    uchar *processedData = new uchar[WIDTH * HEIGHT];
    double *amplitudeTable = new double[WIDTH * HEIGHT];
    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        test[i].real((double) photoData[i]);
    }

    grayFourierArray = new ComplexArray(test, WIDTH * HEIGHT);

    // forward fft
    FFT(grayFourierArray);

    double max = 0;
    double min = 255;
    for (int i = 0; i < WIDTH * HEIGHT; i++)
    {
        amplitudeTable[i] = std::log(std::abs((*grayFourierArray)[i]));

        if (amplitudeTable[i] > max) {
            max = amplitudeTable[i];
        }
        if (amplitudeTable[i] < min) {
            min = amplitudeTable[i];
        }
    }

    for (int i = 0; i < HEIGHT; i++)
    {
        for (int j = 0; j < WIDTH; j++)
        {
            if (i < HEIGHT/2) {
                if (j < WIDTH/2) {
                    processedData[(i + HEIGHT/2) * HEIGHT + j + WIDTH/2] = (uchar) (amplitudeTable[i * HEIGHT + j] - min) * 255 / (max - min);
                } else {
                    processedData[(i + HEIGHT/2) * HEIGHT + j - WIDTH/2] = (uchar) (amplitudeTable[i * HEIGHT + j] - min) * 255 / (max - min);
                }
            } else {
                if (j < WIDTH/2) {
                    processedData[(i - HEIGHT/2) * HEIGHT + j + WIDTH/2] = (uchar) (amplitudeTable[i * HEIGHT + j] - min) * 255 / (max - min);
                } else {
                    processedData[(i - HEIGHT/2) * HEIGHT + j - WIDTH/2] = (uchar) (amplitudeTable[i * HEIGHT + j] - min) * 255 / (max - min);
                }
            }
        }
    }


    newImage = new QImage(processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
    for (int i = 0; i < 256; i++) {
        newImage->setColor(i, qRgb(i, i, i));
    }
    ui->fourierButton->setEnabled(true);
    return newImage;
}

void LowPassFilter::on_fourierButton_clicked()
{

}
