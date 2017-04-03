#include "filtr_median.h"
#include "ui_filtr_median.h"
#include <vector>
#include <algorithm>
#include "../utils.h"

FiltrMedian::FiltrMedian(QWidget *parent) :
    Tool(parent),
    ui(new Ui::FiltrMedian)
{
    ui->setupUi(this);
}

FiltrMedian::FiltrMedian(int width, int height, int depth, QWidget *parent) :
    Tool(width, height, depth, parent),
    ui(new Ui::FiltrMedian)
{
    ui->setupUi(this);
}

FiltrMedian::~FiltrMedian()
{
    delete ui;
}

QImage *FiltrMedian::process(QImage *image)
{
    inputImage = image;

    const int maskWidth = getWidthOfMaskSize();
    const int maskHeight = getHeightOfMaskSize();

    int maskPixelCount = 0;

    if (DEPTH == 8) {
        std::vector<int> pixelsInMaskVector;
        const uchar *photoData = image->bits();
        uchar *processedData = new uchar[WIDTH * HEIGHT];


        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i - maskHeight < 0 || i + maskHeight >= HEIGHT || j - maskWidth < 0 || j + maskWidth >= WIDTH) {
                    processedData[i * HEIGHT + j] = photoData[i * HEIGHT + j];
                    continue;
                }
                maskPixelCount = 0;
                pixelsInMaskVector.clear();
                for (int m = -maskHeight; m <= maskHeight; m++) {
                    for (int n = -maskWidth; n <= maskWidth; n++) {                        
                        pixelsInMaskVector.push_back(photoData[(i+m) * HEIGHT + (j+n)]);
                        maskPixelCount++;
                    }
                }
                // Slow ~1500-2000 nanoseconds
                //std::nth_element(pixelsInMaskVector.begin(), pixelsInMaskVector.begin()+(maskPixelCount / 2), pixelsInMaskVector.end());
                //processedData[i * HEIGHT + j] = (uchar) pixelsInMaskVector[maskPixelCount / 2];

                // Fast ~500-900 nanoseconds
                int *temp = &pixelsInMaskVector[0];
                int median = heapMedian3(temp, maskPixelCount);
                processedData[i * HEIGHT + j] = (uchar) median;
            }
        }
        processedImage = new QImage(processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
        for (int i = 0; i < 256; i++) {
            processedImage->setColor(i, qRgb(i, i, i));
        }
    } else {
        // Obrazy 24 bitowe czyta jako 32...        

        std::vector<int> sumOfSurroundingRedPixels;
        std::vector<int> sumOfSurroundingGreenPixels;
        std::vector<int> sumOfSurroundingBluePixels;
        const QRgb *photoData = (const QRgb *) image->bits();
        QRgb *processedData = new QRgb[WIDTH * HEIGHT];


        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i - maskHeight < 0 || i + maskHeight >= HEIGHT || j - maskWidth < 0 || j + maskWidth >= WIDTH) {
                    processedData[i * HEIGHT + j] = photoData[i * HEIGHT + j];
                    continue;
                }
                sumOfSurroundingRedPixels.clear();
                sumOfSurroundingGreenPixels.clear();
                sumOfSurroundingBluePixels.clear();
                maskPixelCount = 0;
                for (int m = -maskHeight; m <= maskHeight; m++) {
                    for (int n = -maskWidth; n <= maskWidth; n++) {
                        QRgb currentPixel = photoData[(i+m) * HEIGHT + (j+n)];
                        sumOfSurroundingRedPixels.push_back(qRed(currentPixel));
                        sumOfSurroundingGreenPixels.push_back(qGreen(currentPixel));
                        sumOfSurroundingBluePixels.push_back(qBlue(currentPixel));
                        maskPixelCount++;
                    }
                }
                int *temp = &sumOfSurroundingRedPixels[0];
                int medianRed = heapMedian3(temp, maskPixelCount);
                temp = &sumOfSurroundingGreenPixels[0];
                int medianGreen = heapMedian3(temp, maskPixelCount);
                temp = &sumOfSurroundingBluePixels[0];
                int medianBlue = heapMedian3(temp, maskPixelCount);

                processedData[i * HEIGHT + j] = qRgb(medianRed, medianGreen, medianBlue);
            }
        }
        processedImage = new QImage((uchar *)processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
    }

    ui->errorButton->setEnabled(true);
    return processedImage;
}

int FiltrMedian::getWidthOfMaskSize()
{
    return ui->widthValue->value();
}

int FiltrMedian::getHeightOfMaskSize()
{
    return ui->heightValue->value();
}



void FiltrMedian::on_errorButton_clicked()
{
    QString originalFilePath = selectOriginalFile();
    QImage *originalFileImage = new QImage(originalFilePath);

    if (processedImage->width() == originalFileImage->width() &&
        inputImage->width() == originalFileImage->width() &&
        processedImage->height() == originalFileImage->height() &&
        inputImage->height() == originalFileImage->height()) {
        if (processedImage->depth() == originalFileImage->depth() &&
            inputImage->depth() == originalFileImage->depth() &&
            processedImage->depth() == 8) {
            double inputError = 0;
            double processedError = 0;

            inputError = calculateErrorFor8BitImage(originalFileImage, inputImage);
            processedError = calculateErrorFor8BitImage(originalFileImage, processedImage);

            QString errorMessage = "Błąd średniokwadratwy:\n";
            errorMessage.append(" względem wejścia: " + QString::number(inputError) + "\n");
            errorMessage.append(" względem wyjścia: " + QString::number(processedError) + "\n");

            ui->errorValue->setText(errorMessage);
        } else if (processedImage->depth() == originalFileImage->depth() &&
                   inputImage->depth() == originalFileImage->depth() &&
                   processedImage->depth() == 32) {

            double *inputError = calculateErrorFor32BitImage(originalFileImage, inputImage);
            double *processedError = calculateErrorFor32BitImage(originalFileImage, processedImage);

            QString errorMessage = "Błąd średniokwadratwy:\n";
            errorMessage.append("  Względem wejścia:\n");
            errorMessage.append("    czerwony: "+ QString::number(inputError[0]) + "\n");
            errorMessage.append("    zielony: "+ QString::number(inputError[1]) + "\n");
            errorMessage.append("    niebieski: "+ QString::number(inputError[2]) + "\n");
            errorMessage.append("  Względem wyjścia:\n");
            errorMessage.append("    czerwony: "+ QString::number(processedError[0]) + "\n");
            errorMessage.append("    zielony: "+ QString::number(processedError[1]) + "\n");
            errorMessage.append("    niebieski: "+ QString::number(processedError[2]) + "\n");

            ui->errorValue->setText(errorMessage);
        } else {
            ui->errorValue->setText("Nie poprawne kodowanie kolorów.");
        }
    } else {
        ui->errorValue->setText("Nie poprawne rozmiary obrazów.");
    }
}

double FiltrMedian::calculateErrorFor8BitImage(QImage *original, QImage *other)
{
    double error = 0;

    const uchar *originalPixels = original->bits();
    const uchar *otherPixels = other->bits();

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            error += std::pow(originalPixels[i*HEIGHT + j] - otherPixels[i*HEIGHT + j], 2);
        }
    }

    return error / (HEIGHT * WIDTH);
}

double *FiltrMedian::calculateErrorFor32BitImage(QImage *original, QImage *other)
{
    double *error = new double[3] {0};

    const QRgb *originalPixels = (const QRgb *) original->bits();
    const QRgb *otherPixels = (const QRgb *) other->bits();

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            QRgb currentOriginalPixel = originalPixels[i*HEIGHT + j];
            QRgb currentOtherPixel = otherPixels[i*HEIGHT + j];

            error[0] += std::pow(qRed(currentOriginalPixel) - qRed(currentOtherPixel), 2);
            error[1] += std::pow(qGreen(currentOriginalPixel) - qGreen(currentOtherPixel), 2);
            error[2] += std::pow(qBlue(currentOriginalPixel) - qBlue(currentOtherPixel), 2);
        }
    }

    error[0] /= (HEIGHT * WIDTH);
    error[1] /= (HEIGHT * WIDTH);
    error[2] /= (HEIGHT * WIDTH);

    return error;
}

#include <QFileDialog>
#include <QStandardPaths>
#include <QImageReader>
#include <QImageWriter>
void FiltrMedian::initializeImageFileDialog(QFileDialog &dialog, QFileDialog::AcceptMode acceptMode)
{
    static bool firstDialog = true;

    if (firstDialog) {
        firstDialog = false;
        const QStringList picturesLocations = QStandardPaths::standardLocations(QStandardPaths::PicturesLocation);
        dialog.setDirectory(picturesLocations.isEmpty() ? QDir::currentPath() : picturesLocations.last());
    }

    QStringList mimeTypeFilters;
    const QByteArrayList supportedMimeTypes = acceptMode == QFileDialog::AcceptOpen
        ? QImageReader::supportedMimeTypes() : QImageWriter::supportedMimeTypes();
    foreach (const QByteArray &mimeTypeName, supportedMimeTypes)
        mimeTypeFilters.append(mimeTypeName);
    mimeTypeFilters.sort();
    dialog.setMimeTypeFilters(mimeTypeFilters);
    dialog.selectMimeTypeFilter("image/bmp");
    if (acceptMode == QFileDialog::AcceptSave)
        dialog.setDefaultSuffix("bmp");
}

QString FiltrMedian::selectOriginalFile()
{
    QFileDialog dialog(this, tr("Open File"));
    initializeImageFileDialog(dialog, QFileDialog::AcceptOpen);

    if (dialog.exec()) {
        return dialog.selectedFiles().first();
    } else {
        return NULL;
    }
}
