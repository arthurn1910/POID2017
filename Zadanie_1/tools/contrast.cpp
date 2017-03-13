#include "contrast.h"
#include "ui_contrast.h"

Contrast::Contrast(QWidget *parent) :
    Tool(parent),
    ui(new Ui::Contrast)
{
    ui->setupUi(this);
}

Contrast::Contrast(int width, int height, int depth, QWidget *parent) :
    Tool(width, height, depth, parent),
    ui(new Ui::Contrast)
{
    ui->setupUi(this);
    slider = ui->slider;
}

Contrast::~Contrast()
{
    delete ui;
}

QImage *Contrast::process(QImage *image)
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

void Contrast::setDefaultParameters()
{
    slider->setValue(0);
}

int Contrast::getContrastLevel()
{
    return slider->value();
}

void Contrast::updateLookUpTable()
{
    double contrastLevel = getContrastLevel();
    contrastLevel = 1 + contrastLevel / 256.0;

    for (int i = 0; i < LOOK_UP_TABLE_SIZE; i++)
    {
        int pixelValue = contrastLevel * (i - 127) + 127;
        if (pixelValue < 0)
        {
            lookUpTable[i] = (uchar) 0;
        } else if (pixelValue > 255)
        {
            lookUpTable[i] = (uchar) 255;
        } else {
            lookUpTable[i] = (uchar) pixelValue;
        }
    }
}

