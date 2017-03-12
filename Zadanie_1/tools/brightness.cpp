#include "brightness.h"
#include "ui_brightness.h"

Brightness::Brightness(QWidget *parent) :
    Tool(parent),
    ui(new Ui::Brightness)
{
    ui->setupUi(this);
}
Brightness::Brightness(int width, int height, int depth, QWidget *parent) :
    Tool(width, height, depth, parent),
    ui(new Ui::Brightness)
{
    ui->setupUi(this);
    slider = ui->slider;
}

Brightness::~Brightness()
{
    delete ui;
}

#include <iostream>
QImage *Brightness::process(QImage *image)
{
    updateLookUpTable();
    QImage *newImage;

    if (DEPTH == 8) {
        newImage = new QImage(*image);
        int colors = image->colorCount();

        for (int i = 0; i < colors; i++) {
            QRgb old = image->color(i);
            int colorValue = lookUpTable[qGreen(old)];
            QRgb value = qRgb(colorValue, colorValue, colorValue);
            newImage->setColor(i, value);
        }
    } else {
        // Obrazy 24 bitowe czyta jako 32...

//      Metoda 1
//        const int PIXELS = WIDTH * HEIGHT * DEPTH / 8;
//        uchar *photoData = image->bits();
//        uchar *processedData = new uchar[PIXELS];
//        for (int i = 0; i < PIXELS; i++) {
//            processedData[i] = lookUpTable[photoData[i]];
//        }
//        newImage = new QImage(processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());

//      Metoda 2
        QRgb processedPixel;
        newImage = new QImage(WIDTH, HEIGHT, image->format());

        for (int i = 0; i < HEIGHT; i++) {
            QRgb *firstPixelInRow = (QRgb *) image->scanLine(i);
            for (int j = 0; j < WIDTH; j++) {
                QRgb nextPixelInRow = *(firstPixelInRow + j);
                processedPixel = qRgb(lookUpTable[qRed(nextPixelInRow)],
                                      lookUpTable[qGreen(nextPixelInRow)],
                                      lookUpTable[qBlue(nextPixelInRow)]);
                newImage->setPixel(j, i, processedPixel);
            }
        }

    }


    return newImage;
}

void Brightness::setDefaultParameters()
{
    slider->setValue(0);
}

int Brightness::getBrightnessLevel()
{
    return slider->value();
}

void Brightness::updateLookUpTable()
{
    int brightnessLevel = getBrightnessLevel();

    for (int i = 0; i < LOOK_UP_TABLE_SIZE; i++)
    {
        if (i + brightnessLevel < 0)
        {
            lookUpTable[i] = (uchar) 0;
        } else if (i + brightnessLevel > 255)
        {
            lookUpTable[i] = (uchar) 255;
        } else {
            lookUpTable[i] = (uchar) (i + brightnessLevel);
        }
    }
}
