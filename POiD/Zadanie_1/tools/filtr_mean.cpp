#include "filtr_mean.h"
#include "ui_filtr_mean.h"

FiltrMean::FiltrMean(QWidget *parent) :
    Tool(parent),
    ui(new Ui::FiltrMean)
{
    ui->setupUi(this);
}

FiltrMean::FiltrMean(int width, int height, int depth, QWidget *parent) :
    Tool(width, height, depth, parent),
    ui(new Ui::FiltrMean)
{
    ui->setupUi(this);
}

FiltrMean::~FiltrMean()
{
    delete ui;
}

QImage *FiltrMean::process(QImage *image)
{
    inputImage = image;

    const int maskWidth = getWidthOfMaskSize();
    const int maskHeight = getHeightOfMaskSize();

    int maskPixelCount = 0;

    if (DEPTH == 8) {
        int sumOfSurroundingPixels = 0;
        const uchar *photoData = image->bits();
        uchar *processedData = new uchar[WIDTH * HEIGHT];


        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i - maskHeight < 0 || i + maskHeight >= HEIGHT || j - maskWidth < 0 || j + maskWidth >= WIDTH) {
                    processedData[i * HEIGHT + j] = photoData[i * HEIGHT + j];
                    continue;
                }
                sumOfSurroundingPixels = 0;
                maskPixelCount = 0;
                for (int m = -maskHeight; m <= maskHeight; m++) {
                    for (int n = -maskWidth; n <= maskWidth; n++) {
                        sumOfSurroundingPixels += photoData[(i+m) * HEIGHT + (j+n)];
                        maskPixelCount++;
                    }
                }
                processedData[i * HEIGHT + j] = (uchar) (sumOfSurroundingPixels / maskPixelCount);
            }
        }
        processedImage = new QImage(processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
        for (int i = 0; i < 256; i++) {
            processedImage->setColor(i, qRgb(i, i, i));
        }
    } else {
        // Obrazy 24 bitowe czyta jako 32...

        int sumOfSurroundingRedPixels = 0;
        int sumOfSurroundingGreenPixels = 0;
        int sumOfSurroundingBluePixels = 0;
        const QRgb *photoData = (const QRgb *) image->bits();
        QRgb *processedData = new QRgb[WIDTH * HEIGHT];


        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i - maskHeight < 0 || i + maskHeight >= HEIGHT || j - maskWidth < 0 || j + maskWidth >= WIDTH) {
                    processedData[i * HEIGHT + j] = photoData[i * HEIGHT + j];
                    continue;
                }
                sumOfSurroundingRedPixels = 0;
                sumOfSurroundingGreenPixels = 0;
                sumOfSurroundingBluePixels = 0;
                maskPixelCount = 0;
                for (int m = -maskHeight; m <= maskHeight; m++) {
                    for (int n = -maskWidth; n <= maskWidth; n++) {
                        QRgb currentPixel = photoData[(i+m) * HEIGHT + (j+n)];
                        sumOfSurroundingRedPixels += qRed(currentPixel);
                        sumOfSurroundingGreenPixels += qGreen(currentPixel);
                        sumOfSurroundingBluePixels += qBlue(currentPixel);
                        maskPixelCount++;
                    }
                }
                sumOfSurroundingRedPixels /= maskPixelCount;
                sumOfSurroundingGreenPixels /= maskPixelCount;
                sumOfSurroundingBluePixels /= maskPixelCount;
                processedData[i * HEIGHT + j] = qRgb(sumOfSurroundingRedPixels, sumOfSurroundingGreenPixels, sumOfSurroundingBluePixels);
            }
        }
        processedImage = new QImage((uchar *)processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
    }

    ui->errorButton->setEnabled(true);
    return processedImage;
}

int FiltrMean::getWidthOfMaskSize()
{
    return ui->widthValue->value();
}

int FiltrMean::getHeightOfMaskSize()
{
    return ui->heightValue->value();
}


void FiltrMean::on_errorButton_clicked()
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

double FiltrMean::calculateErrorFor8BitImage(QImage *original, QImage *other)
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

double *FiltrMean::calculateErrorFor32BitImage(QImage *original, QImage *other)
{
    double *error = new double[3] {0};

    const QRgb *originalPixels = (const QRgb *) original->bits();
    const QRgb *otherPixels = (const QRgb *) other->bits();

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            QRgb currentOriginalPixel = originalPixels[i*HEIGHT + j];
            QRgb currentOtherPixel = otherPixels[i*HEIGHT + j];

            error[0] += std::pow(qRed(originalPixels[i*HEIGHT + j]) - qRed(otherPixels[i*HEIGHT + j]), 2);
            error[1] += std::pow(qGreen(originalPixels[i*HEIGHT + j]) - qGreen(otherPixels[i*HEIGHT + j]), 2);
            error[2] += std::pow(qBlue(originalPixels[i*HEIGHT + j]) - qBlue(otherPixels[i*HEIGHT + j]), 2);
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
void FiltrMean::initializeImageFileDialog(QFileDialog &dialog, QFileDialog::AcceptMode acceptMode)
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

QString FiltrMean::selectOriginalFile()
{
    QFileDialog dialog(this, tr("Open File"));
    initializeImageFileDialog(dialog, QFileDialog::AcceptOpen);

    if (dialog.exec()) {
        return dialog.selectedFiles().first();
    } else {
        return NULL;
    }
}
