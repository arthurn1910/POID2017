#include "filtr_sobel.h"
#include "ui_filtr_sobel.h"
#include <iostream>

FiltrSobel::FiltrSobel(QWidget *parent) :
    Tool(parent),
    ui(new Ui::FiltrSobel)
{
    ui->setupUi(this);
}

FiltrSobel::FiltrSobel(int width, int height, int depth, QWidget *parent) :
    Tool(width, height, depth, parent),
    ui(new Ui::FiltrSobel)
{
    ui->setupUi(this);
}

FiltrSobel::~FiltrSobel()
{
    delete ui;
}
QImage *FiltrSobel::process(QImage *image)
{
    QImage *newImage;

    const int maskWidth = 1;
    const int maskHeight = 1;


    if (DEPTH == 8) {
        const uchar *photoData = image->bits();
        uchar *processedData = new uchar[WIDTH * HEIGHT];
        double sobelX = 0;
        double sobelY = 0;

        double *sobelTab = new double[WIDTH * HEIGHT];
//        double min = 256;
//        double max = 0;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i - maskHeight < 0 || i + maskHeight >= HEIGHT || j - maskWidth < 0 || j + maskWidth >= WIDTH) {
                    processedData[i * HEIGHT + j] = photoData[i * HEIGHT + j];
                    continue;
                }

                sobelX = photoData[(i-1) * HEIGHT + (j+1)];// A2
                sobelX += 2* photoData[(i) * HEIGHT + (j+1)];// A3
                sobelX += photoData[(i+1) * HEIGHT + (j+1)];// A4
                sobelX -= photoData[(i-1) * HEIGHT + (j-1)];// A0
                sobelX -= 2 * photoData[(i) * HEIGHT + (j-1)];// A7
                sobelX -= photoData[(i+1) * HEIGHT + (j-1)];// A6

                sobelY = photoData[(i-1) * HEIGHT + (j-1)];// A0
                sobelY += 2* photoData[(i-1) * HEIGHT + (j)];// A1
                sobelY += photoData[(i-1) * HEIGHT + (j+1)];// A2
                sobelY -= photoData[(i+1) * HEIGHT + (j-1)];// A6
                sobelY -= 2 * photoData[(i+1) * HEIGHT + (j)];// A5
                sobelY -= photoData[(i+1) * HEIGHT + (j+1)];// A4

                sobelTab[i * HEIGHT + j] = std::sqrt(sobelX * sobelX + sobelY * sobelY);


                if (sobelTab[i * HEIGHT + j] > 255) {
                    sobelTab[i * HEIGHT + j] = 255;
                }
                processedData[i * HEIGHT + j] = (uchar) sobelTab[i * HEIGHT + j];

//                if (sobelTab[i * HEIGHT + j] > max) {
//                    max = sobelTab[i * HEIGHT + j];
//                }
//                if (sobelTab[i * HEIGHT + j] < min) {
//                    min = sobelTab[i * HEIGHT + j];
//                }
            }
        }
        //if (max > 255 || min < 0) {
//            for (int i = 0; i < HEIGHT; i++) {
//                for (int j = 0; j < WIDTH; j++) {
//                    processedData[i * HEIGHT + j] = (uchar) (sobelTab[i * HEIGHT + j] - min) * 255.0 /(max-min);
//                }
//            }
        //}
        newImage = new QImage(processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
        for (int i = 0; i < 256; i++) {
            newImage->setColor(i, qRgb(i, i, i));
        }
    } else {
        // Obrazy 24 bitowe czyta jako 32...

        const QRgb *photoData = (const QRgb *) image->bits();
        QRgb *processedData = new QRgb[WIDTH * HEIGHT];
        double sobelRedX = 0;
        double sobelRedY = 0;
        double sobelGreenX = 0;
        double sobelGreenY = 0;
        double sobelBlueX = 0;
        double sobelBlueY = 0;

        int **sobelTab = new int*[HEIGHT * WIDTH];
        for (int i = 0; i < HEIGHT * WIDTH; i++) {
            sobelTab[i] = new int[3];
        }


//        double minRed = 256;
//        double maxRed = 0;
//        double minGreen = 256;
//        double maxGreen = 0;
//        double minBlue = 256;
//        double maxBlue = 0;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i - maskHeight < 0 || i + maskHeight >= HEIGHT || j - maskWidth < 0 || j + maskWidth >= WIDTH) {
                    processedData[i * HEIGHT + j] = photoData[i * HEIGHT + j];
                    continue;
                }

                QRgb a0Pixel = photoData[(i-1) * HEIGHT + (j-1)];
                QRgb a1Pixel = photoData[(i-1) * HEIGHT + (j)];
                QRgb a2Pixel = photoData[(i-1) * HEIGHT + (j+1)];
                QRgb a3Pixel = photoData[(i) * HEIGHT + (j+1)];
                QRgb a4Pixel = photoData[(i+1) * HEIGHT + (j+1)];
                QRgb a5Pixel = photoData[(i+1) * HEIGHT + (j)];
                QRgb a6Pixel = photoData[(i+1) * HEIGHT + (j-1)];
                QRgb a7Pixel = photoData[(i) * HEIGHT + (j-1)];


                sobelRedX = qRed(a2Pixel) + 2 * qRed(a3Pixel) + qRed(a4Pixel) - qRed(a0Pixel) - 2 * qRed(a7Pixel) - qRed(a6Pixel);
                sobelRedY = qRed(a0Pixel) + 2 * qRed(a1Pixel) + qRed(a2Pixel) - qRed(a6Pixel) - 2 * qRed(a5Pixel) - qRed(a4Pixel);
                sobelTab[i * HEIGHT + j][0] = std::sqrt(sobelRedX * sobelRedX + sobelRedY * sobelRedY);
                if (sobelTab[i * HEIGHT + j][0] > 255) {
                    sobelTab[i * HEIGHT + j][0] = 255;
                }

                sobelGreenX = qGreen(a2Pixel) + 2 * qGreen(a3Pixel) + qGreen(a4Pixel) - qGreen(a0Pixel) - 2 * qGreen(a7Pixel) - qGreen(a6Pixel);
                sobelGreenY = qGreen(a0Pixel) + 2 * qGreen(a1Pixel) + qGreen(a2Pixel) - qGreen(a6Pixel) - 2 * qGreen(a5Pixel) - qGreen(a4Pixel);
                sobelTab[i * HEIGHT + j][1] = std::sqrt(sobelGreenX * sobelGreenX + sobelGreenY * sobelGreenY);
                if (sobelTab[i * HEIGHT + j][1] > 255) {
                    sobelTab[i * HEIGHT + j][1] = 255;
                }

                sobelBlueX = qBlue(a2Pixel) + 2 * qBlue(a3Pixel) + qBlue(a4Pixel) - qBlue(a0Pixel) - 2 * qBlue(a7Pixel) - qBlue(a6Pixel);
                sobelBlueY = qBlue(a0Pixel) + 2 * qBlue(a1Pixel) + qBlue(a2Pixel) - qBlue(a6Pixel) - 2 * qBlue(a5Pixel) - qBlue(a4Pixel);
                sobelTab[i * HEIGHT + j][2] = std::sqrt(sobelBlueX * sobelBlueX + sobelBlueY * sobelBlueY);
                if (sobelTab[i * HEIGHT + j][2] > 255) {
                    sobelTab[i * HEIGHT + j][2] = 255;
                }

                processedData[i * HEIGHT + j] = qRgb(sobelTab[i * HEIGHT + j][0], sobelTab[i * HEIGHT + j][1], sobelTab[i * HEIGHT + j][2]);
            }
        }
//        for (int i = 0; i < HEIGHT; i++) {
//            for (int j = 0; j < WIDTH; j++) {
//                double red = array[i * HEIGHT + j][0];
//                red = (red - minRed) * 255 /(maxRed-minRed);


//                double green = array[i * HEIGHT + j][1];
//                green = (green - minGreen) * 255 /(maxGreen-minGreen);


//                double blue = array[i * HEIGHT + j][2];
//                blue = (blue - minBlue) * 255 /(maxBlue-minBlue);


//                processedData[i * HEIGHT + j] = qRgb(red, green, blue);
//            }
//        }
        newImage = new QImage((uchar *)processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
    }

    return newImage;
}

