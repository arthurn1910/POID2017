#include "filtr_linear_splot.h"
#include "ui_filtr_linear_splot.h"

FiltrLinearSplot::FiltrLinearSplot(QWidget *parent) :
    Tool(parent),
    ui(new Ui::FiltrLinearSplot)
{
    ui->setupUi(this);
}

FiltrLinearSplot::FiltrLinearSplot(int width, int height, int depth, QWidget *parent) :
    Tool(width, height, depth, parent),
    ui(new Ui::FiltrLinearSplot)
{
    ui->setupUi(this);
}

FiltrLinearSplot::~FiltrLinearSplot()
{
    delete ui;
}
#include <QErrorMessage>
#include <iostream>
QImage *FiltrLinearSplot::process(QImage *image)
{
    //updateLookUpTable();
    QImage *newImage;

    const int maskWidth = getWidthOfMaskSize();
    const int maskHeight = getHeightOfMaskSize();

    int *mask = parseFiltrMask(ui->maskParameters->toPlainText());

    if (mask == nullptr) {
        QErrorMessage *noForColorImages = new QErrorMessage();
        noForColorImages->showMessage("Zła macierz maski!");
        return image;
    }

    int maskWeightSum = 0;

    if (DEPTH == 8) {
        int sumOfSurroundingPixels = 0;
        const uchar *photoData = image->bits();
        uchar *processedData = new uchar[WIDTH * HEIGHT];

        int min = 256;
        int max = 0;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i - maskHeight < 0 || i + maskHeight >= HEIGHT || j - maskWidth < 0 || j + maskWidth >= WIDTH) {
                    processedData[i * HEIGHT + j] = photoData[i * HEIGHT + j];
                    continue;
                }
                sumOfSurroundingPixels = 0;
                maskWeightSum = 0;
                for (int m = -maskHeight; m <= maskHeight; m++) {
                    for (int n = -maskWidth; n <= maskWidth; n++) {
                        int maskElement = (maskHeight + m) * (maskHeight * 2 + 1) + maskWidth + n;
                        sumOfSurroundingPixels += photoData[(i+m) * HEIGHT + (j+n)] * mask[maskElement];
                        maskWeightSum += mask[maskElement];
                    }
                }
                if (maskWeightSum == 0) {
                    maskWeightSum = 1;
                }
                if (sumOfSurroundingPixels / maskWeightSum > max) {
                    max = sumOfSurroundingPixels / maskWeightSum;
                } else if (sumOfSurroundingPixels / maskWeightSum < min) {
                    min = sumOfSurroundingPixels / maskWeightSum;
                }
                processedData[i * HEIGHT + j] = (uchar) (sumOfSurroundingPixels / maskWeightSum);
            }
        }
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                processedData[i * HEIGHT + j] = (uchar) (processedData[i * HEIGHT + j] - min) * 255 /(max-min);
            }
        }
        newImage = new QImage(processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
        for (int i = 0; i < 256; i++) {
            newImage->setColor(i, qRgb(i, i, i));
        }
    } else {
        // Obrazy 24 bitowe czyta jako 32...

        int sumOfSurroundingRedPixels = 0;
        int sumOfSurroundingGreenPixels = 0;
        int sumOfSurroundingBluePixels = 0;
        const QRgb *photoData = (const QRgb *) image->bits();
        QRgb *processedData = new QRgb[WIDTH * HEIGHT];
        int **array = new int*[HEIGHT * WIDTH];
        for (int i = 0; i < HEIGHT * WIDTH; i++) {
            array[i] = new int[3];
        }


        double minRed = 256;
        double maxRed = 0;
        double minGreen = 256;
        double maxGreen = 0;
        double minBlue = 256;
        double maxBlue = 0;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i - maskHeight < 0 || i + maskHeight >= HEIGHT || j - maskWidth < 0 || j + maskWidth >= WIDTH) {
                    processedData[i * HEIGHT + j] = photoData[i * HEIGHT + j];
                    continue;
                }
                sumOfSurroundingRedPixels = 0;
                sumOfSurroundingGreenPixels = 0;
                sumOfSurroundingBluePixels = 0;
                maskWeightSum = 0;
                for (int m = -maskHeight; m <= maskHeight; m++) {
                    for (int n = -maskWidth; n <= maskWidth; n++) {
                        int maskElement = (maskHeight + m) * (maskHeight * 2 + 1) + maskWidth + n;
                        QRgb currentPixel = photoData[(i+m) * HEIGHT + (j+n)];
                        sumOfSurroundingRedPixels += qRed(currentPixel) * mask[maskElement];
                        sumOfSurroundingGreenPixels += qGreen(currentPixel) * mask[maskElement];
                        sumOfSurroundingBluePixels += qBlue(currentPixel) * mask[maskElement];
                        maskWeightSum += mask[maskElement];
                    }
                }
                if (maskWeightSum == 0) {
                    maskWeightSum = 1;
                }
                sumOfSurroundingRedPixels /= maskWeightSum;
                if (sumOfSurroundingRedPixels > maxRed) {
                    maxRed = sumOfSurroundingRedPixels;
                } else if (sumOfSurroundingRedPixels < minRed) {
                    minRed = sumOfSurroundingRedPixels;
                }

                sumOfSurroundingGreenPixels /= maskWeightSum;
                if (sumOfSurroundingGreenPixels > maxGreen) {
                    maxGreen = sumOfSurroundingGreenPixels;
                } else if (sumOfSurroundingGreenPixels < minGreen) {
                    minGreen = sumOfSurroundingGreenPixels;
                }

                sumOfSurroundingBluePixels /= maskWeightSum;
                if (sumOfSurroundingBluePixels > maxBlue) {
                    maxBlue = sumOfSurroundingBluePixels;
                } else if (sumOfSurroundingBluePixels < minBlue) {
                    minBlue = sumOfSurroundingBluePixels;
                }

                //std::cout << sumOfSurroundingRedPixels << " - " << sumOfSurroundingGreenPixels << " - " << sumOfSurroundingBluePixels << "\n";
                array[i * HEIGHT + j][0] = sumOfSurroundingRedPixels;
                array[i * HEIGHT + j][1] = sumOfSurroundingGreenPixels;
                array[i * HEIGHT + j][2] = sumOfSurroundingBluePixels;

                //processedData[i * HEIGHT + j] = qRgb(sumOfSurroundingRedPixels, sumOfSurroundingGreenPixels, sumOfSurroundingBluePixels);
            }
        }        
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {

                double red = array[i * HEIGHT + j][0];
                red = (red - minRed) * 255 /(maxRed-minRed);


                double green = array[i * HEIGHT + j][1];
                green = (green - minGreen) * 255 /(maxGreen-minGreen);


                double blue = array[i * HEIGHT + j][2];
                blue = (blue - minBlue) * 255 /(maxBlue-minBlue);


                processedData[i * HEIGHT + j] = qRgb(red, green, blue);
            }
        }
        newImage = new QImage((uchar *)processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
    }

    return newImage;
}

int *FiltrLinearSplot::parseFiltrMask(QString filtrMask)
{
    QStringList lines = filtrMask.split("\n");
    int height = getHeightOfMaskSize() * 2 + 1;
    int width = getWidthOfMaskSize() * 2 + 1;
    if (lines.size() != height) {
        return nullptr;
    }
    int *mask = new int[width * height];
    for (int i = 0; i < height; i++) {
        QStringList elements = lines[i].split(";");
        if (elements.size() != width) {
            delete mask;
            return nullptr;
        }
        for (int j = 0; j < width; j++) {
            mask[i * height + j] = elements[j].toInt();
        }
    }
    return mask;
}

int FiltrLinearSplot::getWidthOfMaskSize()
{
    return ui->widthValue->value();
}

int FiltrLinearSplot::getHeightOfMaskSize()
{
    return ui->heightValue->value();
}


void FiltrLinearSplot::on_northRadioButton_clicked(bool checked)
{
    if (checked) {
        QString northMask = "1;1;1\n1;-2;1\n-1;-1;-1";
        ui->maskParameters->setText(northMask);
        ui->heightValue->setValue(1);
        ui->widthValue->setValue(1);
    }
}

void FiltrLinearSplot::on_northEastRadioButton_clicked(bool checked)
{
    if (checked) {
        QString northEastMask = "1;1;1\n-1;-2;1\n-1;-1;1";
        ui->maskParameters->setText(northEastMask);
        ui->heightValue->setValue(1);
        ui->widthValue->setValue(1);
    }
}

void FiltrLinearSplot::on_eastRadioButton_clicked(bool checked)
{
    if (checked) {
        QString eastMask = "-1;1;1\n-1;-2;1\n-1;1;1";
        ui->maskParameters->setText(eastMask);
        ui->heightValue->setValue(1);
        ui->widthValue->setValue(1);
    }
}

void FiltrLinearSplot::on_southEastRadioButton_clicked(bool checked)
{
    if (checked) {
        QString southEastMask = "-1;-1;1\n-1;-2;1\n1;1;1";
        ui->maskParameters->setText(southEastMask);
        ui->heightValue->setValue(1);
        ui->widthValue->setValue(1);
    }
}
