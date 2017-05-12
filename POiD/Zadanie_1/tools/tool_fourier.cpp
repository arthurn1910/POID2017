#define _USE_MATH_DEFINES

#include "tool_fourier.h"
#include "zadanie2/spectrum_window.h"
#include <cmath>
#include <QDebug>

ToolFourier::ToolFourier(QWidget *parent) :
    Tool(parent)
{

}

ToolFourier::ToolFourier(int width, int height, int depth, QWidget *parent) :
    Tool(width, height, depth, parent)
{

}

ToolFourier::~ToolFourier()
{

}

QImage *ToolFourier::process(QImage *image)
{
    if (DEPTH == 8) {
        return process8BitImage(image);
    } else {
        return process32BitImage(image);
    }
}

void ToolFourier::FFT(std::complex<double> *input, const int size)
{
    if (size <= 1)
        return;

    // divide
    std::complex<double> *even = new std::complex<double>[size/2];
    std::complex<double>  *odd = new std::complex<double>[size/2];
    int evenCounter = 0;
    int oddCounter = 0;
    for (int i = 0; i < size; i++) {
        if (i % 2 == 0) {
            even[evenCounter++] = input[i];
        } else {
            odd[oddCounter++] = input[i];
        }
    }

    // conquer
    FFT(even, size/2);
    FFT(odd, size/2);

    // combine
    for (int k = 0; k < size/2; k++)
    {
        std::complex<double> complexNumber(0, -2 * M_PI * k / size);
        //complexNumber.real(0);
        //complexNumber.imag(-2 * M_PI * k / N);
        std::complex<double> t = std::exp(complexNumber) * odd[k];
        input[k    ] = even[k] + t;
        input[k+size/2] = even[k] - t;
    }

    delete [] even;
    delete [] odd;
}

QImage *ToolFourier::process8BitImage(QImage *originalPhoto)
{
    QImage *processedImage;
    const uchar *photoPixels = originalPhoto->bits();
    uchar *processedPhotoPixels = new uchar[WIDTH * HEIGHT];

    if (grayOriginalFFT == nullptr) {
        grayOriginalFFT = new std::complex<double>[WIDTH * HEIGHT];
        grayMaskFFT = new std::complex<double>[WIDTH * HEIGHT];

        for (int i = 0; i < WIDTH*HEIGHT; i++) {
            grayOriginalFFT[i].real((double) photoPixels[i]);
            grayOriginalFFT[i].imag(0);
            grayMaskFFT[i].real(0);
            grayMaskFFT[i].imag(0);
        }

        FFT(grayOriginalFFT, WIDTH * HEIGHT);

        moveToCenter(grayOriginalFFT);
    }

    createMask(grayMaskFFT);
    std::complex<double> *grayWithMaskFFT = applayMask(grayOriginalFFT, grayMaskFFT);

    moveToCorners(grayWithMaskFFT);

    IFFT(grayWithMaskFFT, WIDTH * HEIGHT);

    for (int i = 0; i < WIDTH*HEIGHT; i++) {
        double val = std::abs(grayWithMaskFFT[i]);
        if(val > 255) {
            val = 255;
        }
        processedPhotoPixels[i] = (uchar) val;
        if(processedPhotoPixels[i] > 255) {
            processedPhotoPixels[i] = 255;
        }
    }

    processedImage = new QImage(processedPhotoPixels, WIDTH, HEIGHT, originalPhoto->bytesPerLine(), originalPhoto->format());
    for (int i = 0; i < 256; i++) {
        processedImage->setColor(i, qRgb(i, i, i));
    }
    return processedImage;
}

QImage *ToolFourier::process32BitImage(QImage *originalPhoto)
{
    QImage *processedImage = new QImage(WIDTH, HEIGHT, QImage::Format_RGB888);
    const QRgb *photoPixels = (const QRgb *) originalPhoto->bits();

    if (redOriginalFFT == nullptr) {
        // RED
        redOriginalFFT = new std::complex<double>[WIDTH * HEIGHT];
        redMaskFFT = new std::complex<double>[WIDTH * HEIGHT];
        for (int i = 0; i < WIDTH*HEIGHT; i++) {
            redOriginalFFT[i].real(qRed(photoPixels[i]));
        }
        FFT(redOriginalFFT, WIDTH * HEIGHT);
        moveToCenter(redOriginalFFT);

        // GREEN
        greenOriginalFFT = new std::complex<double>[WIDTH * HEIGHT];
        greenMaskFFT = new std::complex<double>[WIDTH * HEIGHT];
        for (int i = 0; i < WIDTH*HEIGHT; i++) {
            greenOriginalFFT[i].real(qGreen(photoPixels[i]));
        }
        FFT(greenOriginalFFT, WIDTH * HEIGHT);
        moveToCenter(greenOriginalFFT);

        //BLUE
        blueOriginalFFT = new std::complex<double>[WIDTH * HEIGHT];
        blueMaskFFT = new std::complex<double>[WIDTH * HEIGHT];
        for (int i = 0; i < WIDTH*HEIGHT; i++) {
            blueOriginalFFT[i].real(qBlue(photoPixels[i]));
        }
        FFT(blueOriginalFFT, WIDTH * HEIGHT);
        moveToCenter(blueOriginalFFT);
    }

    createMask(redMaskFFT);
    std::complex<double> *redWithMaskFFT = applayMask(redOriginalFFT, redMaskFFT);

    createMask(greenMaskFFT);
    std::complex<double> *greenWithMaskFFT = applayMask(greenOriginalFFT, greenMaskFFT);

    createMask(blueMaskFFT);
    std::complex<double> *blueWithMaskFFT = applayMask(blueOriginalFFT, blueMaskFFT);

    moveToCorners(redWithMaskFFT);
    moveToCorners(greenWithMaskFFT);
    moveToCorners(blueWithMaskFFT);

    IFFT(redWithMaskFFT, WIDTH * HEIGHT);
    IFFT(greenWithMaskFFT, WIDTH * HEIGHT);
    IFFT(blueWithMaskFFT, WIDTH * HEIGHT);

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            double red = std::abs(redWithMaskFFT[i * HEIGHT + j]);
            if(red > 255) {
                red = 255;
            }

            double green = std::abs(greenWithMaskFFT[i * HEIGHT + j]);
            if(green > 255) {
                green = 255;
            }

            double blue = std::abs(blueWithMaskFFT[i * HEIGHT + j]);
            if(blue > 255) {
                blue = 255;
            }

            processedImage->setPixel(j, i, qRgb(red, green, blue));
        }
    }

    delete [] redWithMaskFFT;
    delete [] greenWithMaskFFT;
    delete [] blueWithMaskFFT;

    return processedImage;
}

void ToolFourier::IFFT(std::complex<double> *input, const int size)
{
    for (int i = 0; i < size; i++) {
        input[i] = std::conj(input[i]);
    }

    // forward fft
    FFT(input, size);

    for (int i = 0; i < size; i++) {
        input[i] = std::conj(input[i]);
    }

    for (int i = 0; i < size; i++) {
        input[i] /= size;
    }
}

void ToolFourier::moveToCenter(std::complex<double> *fftArray)
{
    for (int i = 0; i < HEIGHT / 2; i++)
    {
        for (int j = 0; j < WIDTH; j++)
        {
            if (j < WIDTH/2) {
                std::complex<double> temporary = fftArray[(i + HEIGHT/2) * HEIGHT + j + WIDTH/2];
                fftArray[(i + HEIGHT/2) * HEIGHT + j + WIDTH/2] = fftArray[i * HEIGHT + j];
                fftArray[i * HEIGHT + j] = temporary;
            } else {
                std::complex<double> temporary = fftArray[(i + HEIGHT/2) * HEIGHT + j - WIDTH/2];
                fftArray[(i + HEIGHT/2) * HEIGHT + j - WIDTH/2] = fftArray[i * HEIGHT + j];
                fftArray[i * HEIGHT + j] = temporary;
            }
        }
    }
}

void ToolFourier::moveToCorners(std::complex<double> *fftArray)
{
    moveToCenter(fftArray);
}

std::complex<double> *ToolFourier::applayMask(const std::complex<double> *originalFFT, const std::complex<double> *maskFFT)
{
    std::complex<double> *withMaskFFT = new std::complex<double>[WIDTH * HEIGHT];

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            withMaskFFT[i * WIDTH + j] = maskFFT[i * WIDTH + j] * originalFFT[i * WIDTH + j];
        }
    }

    return withMaskFFT;
}

void ToolFourier::on_amplitudeButton_clicked()
{
    if (DEPTH == 8) {
        createAmplitudeSpectrumGray();
        createAmplitudeSpectrumWithMaskGray();
    } else {
        createAmplitudeSpectrumColor();
        createAmplitudeSpectrumWithMaskColor();
    }

    SpectrumWindow *window = new SpectrumWindow(this);
    window->setOriginalSpectrum(amplitudeImage);
    window->setWithMaskSpectrum(amplitudeWithMaskImage);
    window->show();
}

void ToolFourier::createAmplitudeSpectrumGray()
{
    double *amplitudeData = new double[WIDTH * HEIGHT];
    uchar *imageData = new uchar[WIDTH * HEIGHT];

    maxValueForNormalizationGray = 0;
    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        amplitudeData[i] = std::log(1 + std::abs(grayOriginalFFT[i]));

        if (maxValueForNormalizationGray < amplitudeData[i]) {
            maxValueForNormalizationGray = amplitudeData[i];
        }
    }

    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        imageData[i] = amplitudeData[i] * 255 / maxValueForNormalizationGray;
    }
    delete [] amplitudeData;

    amplitudeImage = new QImage(imageData, WIDTH, HEIGHT, WIDTH, QImage::Format_Grayscale8);
    for (int i = 0; i < 256; i++) {
        amplitudeImage->setColor(i, qRgb(i, i, i));
    }
}

void ToolFourier::createAmplitudeSpectrumWithMaskGray()
{
    double *amplitudeWithMaskData = new double[WIDTH * HEIGHT];
    uchar *imageWithMaskData = new uchar[WIDTH * HEIGHT];
    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        amplitudeWithMaskData[i] = std::log(1 + std::abs((grayOriginalFFT[i] * grayMaskFFT[i])));
    }

    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        imageWithMaskData[i] = (uchar) amplitudeWithMaskData[i] * 255 / maxValueForNormalizationGray;
    }
    delete [] amplitudeWithMaskData;

    amplitudeWithMaskImage = new QImage(imageWithMaskData, WIDTH, HEIGHT, WIDTH, QImage::Format_Grayscale8);
    for (int i = 0; i < 256; i++) {
        amplitudeWithMaskImage->setColor(i, qRgb(i, i, i));
    }
}

void ToolFourier::createAmplitudeSpectrumColor()
{
    amplitudeImage = new QImage(WIDTH, HEIGHT, QImage::Format_RGB888);

    double **amplitudeData = new double*[3];
    amplitudeData[0] = new double[WIDTH * HEIGHT]; // R
    amplitudeData[1] = new double[WIDTH * HEIGHT]; // G
    amplitudeData[2] = new double[WIDTH * HEIGHT]; // B

    maxValueForNormalizationColor[0] = 0;
    maxValueForNormalizationColor[1] = 0;
    maxValueForNormalizationColor[2] = 0;
    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        // RED
        amplitudeData[0][i] = std::log(1 + std::abs(redOriginalFFT[i]));

        if (maxValueForNormalizationColor[0] < amplitudeData[0][i]) {
            maxValueForNormalizationColor[0] = amplitudeData[0][i];
        }
        // GREEN
        amplitudeData[1][i] = std::log(1 + std::abs(greenOriginalFFT[i]));

        if (maxValueForNormalizationColor[1] < amplitudeData[1][i]) {
            maxValueForNormalizationColor[1] = amplitudeData[1][i];
        }
        //BLUE
        amplitudeData[2][i] = std::log(1 + std::abs(blueOriginalFFT[i]));

        if (maxValueForNormalizationColor[2] < amplitudeData[2][i]) {
            maxValueForNormalizationColor[2] = amplitudeData[2][i];
        }
    }

    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        amplitudeData[0][i] = (amplitudeData[0][i]) * 255 / (maxValueForNormalizationColor[0]);
        amplitudeData[1][i] = (amplitudeData[1][i]) * 255 / (maxValueForNormalizationColor[1]);
        amplitudeData[2][i] = (amplitudeData[2][i]) * 255 / (maxValueForNormalizationColor[2]);
    }

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            amplitudeImage->setPixel(j, i, qRgb(amplitudeData[0][i * HEIGHT + j], amplitudeData[1][i * HEIGHT + j], amplitudeData[2][i * HEIGHT + j]));
        }
    }

    delete [] amplitudeData[0];
    delete [] amplitudeData[1];
    delete [] amplitudeData[2];
    delete [] amplitudeData;
}

void ToolFourier::createAmplitudeSpectrumWithMaskColor()
{
    amplitudeWithMaskImage = new QImage(WIDTH, HEIGHT, QImage::Format_RGB888);

    double **amplitudeWithMaskData = new double*[3];
    amplitudeWithMaskData[0] = new double[WIDTH * HEIGHT]; // R
    amplitudeWithMaskData[1] = new double[WIDTH * HEIGHT]; // G
    amplitudeWithMaskData[2] = new double[WIDTH * HEIGHT]; // B

    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        // RED
        amplitudeWithMaskData[0][i] = std::log(1 + std::abs(redOriginalFFT[i] * redMaskFFT[i]));
        // GREEN
        amplitudeWithMaskData[1][i] = std::log(1 + std::abs(greenOriginalFFT[i] * greenMaskFFT[i]));
        //BLUE
        amplitudeWithMaskData[2][i] = std::log(1 + std::abs(blueOriginalFFT[i] * blueMaskFFT[i]));
    }

    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        amplitudeWithMaskData[0][i] = (amplitudeWithMaskData[0][i]) * 255 / (maxValueForNormalizationColor[0]);
        amplitudeWithMaskData[1][i] = (amplitudeWithMaskData[1][i]) * 255 / (maxValueForNormalizationColor[1]);
        amplitudeWithMaskData[2][i] = (amplitudeWithMaskData[2][i]) * 255 / (maxValueForNormalizationColor[2]);
    }

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            amplitudeWithMaskImage->setPixel(j, i, qRgb(amplitudeWithMaskData[0][i * HEIGHT + j], amplitudeWithMaskData[1][i * HEIGHT + j], amplitudeWithMaskData[2][i * HEIGHT + j]));
        }
    }

    delete [] amplitudeWithMaskData[0];
    delete [] amplitudeWithMaskData[1];
    delete [] amplitudeWithMaskData[2];
    delete [] amplitudeWithMaskData;
}


void ToolFourier::on_phaseButton_clicked()
{
    if (DEPTH == 8) {
        createPhaseSpectrumGray();
        createPhaseSpectrumWithMaskGray();
    } else {
        createPhaseSpectrumColor();
        createPhaseSpectrumWithMaskColor();
    }

    SpectrumWindow *window = new SpectrumWindow(this);
    window->setOriginalSpectrum(phaseImage);
    window->setWithMaskSpectrum(phaseWithMaskImage);
    window->show();
}

void ToolFourier::createPhaseSpectrumGray()
{
    double *phaseData = new double[WIDTH * HEIGHT];
    uchar *imageData = new uchar[WIDTH * HEIGHT];

    maxValueForNormalizationGray = 0;
    minValueForNormalizationGray = 255;

    moveToCorners(grayOriginalFFT);
    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        phaseData[i] = std::arg(grayOriginalFFT[i]);

        if (maxValueForNormalizationGray < phaseData[i]) {
            maxValueForNormalizationGray = phaseData[i];
        }
        if (minValueForNormalizationGray > phaseData[i]) {
            minValueForNormalizationGray = phaseData[i];
        }
    }
    moveToCenter(grayOriginalFFT);

    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        imageData[i] = (uchar) (phaseData[i] - minValueForNormalizationGray) * 255 / (maxValueForNormalizationGray - minValueForNormalizationGray);
    }

    phaseImage = new QImage(imageData, WIDTH, HEIGHT, WIDTH, QImage::Format_Grayscale8);
    for (int i = 0; i < 256; i++) {
        phaseImage->setColor(i, qRgb(i, i, i));
    }
}
#include <iostream>
void ToolFourier::createPhaseSpectrumWithMaskGray()
{
    double *phaseWithMaskData = new double[WIDTH * HEIGHT];
    uchar *imageWithMaskData = new uchar[WIDTH * HEIGHT];

    moveToCorners(grayOriginalFFT);
    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        phaseWithMaskData[i] = std::arg(grayOriginalFFT[i] * grayMaskFFT[i]);
    }
    moveToCenter(grayOriginalFFT);

    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        imageWithMaskData[i] = (phaseWithMaskData[i] - minValueForNormalizationGray) * 255 / (maxValueForNormalizationGray - minValueForNormalizationGray);
    }

    phaseWithMaskImage = new QImage(imageWithMaskData, WIDTH, HEIGHT, WIDTH, QImage::Format_Grayscale8);
    for (int i = 0; i < 256; i++) {
        phaseWithMaskImage->setColor(i, qRgb(i, i, i));
    }
}

void ToolFourier::createPhaseSpectrumColor()
{
    phaseImage = new QImage(WIDTH, HEIGHT, QImage::Format_RGB888);

    double **phaseData = new double*[3];
    phaseData[0] = new double[WIDTH * HEIGHT];
    phaseData[1] = new double[WIDTH * HEIGHT];
    phaseData[2] = new double[WIDTH * HEIGHT];

    double max[3] {0};
    double min[3] {255};
    moveToCorners(redOriginalFFT);
    moveToCorners(greenOriginalFFT);
    moveToCorners(blueOriginalFFT);
    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        // RED
        phaseData[0][i] = std::arg(redOriginalFFT[i]);

        if (max[0] < phaseData[0][i]) {
            max[0] = phaseData[0][i];
        }
        if (min[0] > phaseData[0][i]) {
            min[0] = phaseData[0][i];
        }
        // GREEN
        phaseData[1][i] = std::arg(greenOriginalFFT[i]);

        if (max[1] < phaseData[1][i]) {
            max[1] = phaseData[1][i];
        }
        if (min[1] > phaseData[1][i]) {
            min[1] = phaseData[1][i];
        }
        //BLUE
        phaseData[2][i] = std::arg(blueOriginalFFT[i]);

        if (max[2] < phaseData[2][i]) {
            max[2] = phaseData[2][i];
        }
        if (min[2] > phaseData[2][i]) {
            min[2] = phaseData[2][i];
        }
    }
    moveToCenter(redOriginalFFT);
    moveToCenter(greenOriginalFFT);
    moveToCenter(blueOriginalFFT);

    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        phaseData[0][i] = (phaseData[0][i] - min[0]) * 255 / (max[0] - min[0]);
        phaseData[1][i] = (phaseData[1][i] - min[1]) * 255 / (max[1] - min[1]);
        phaseData[2][i] = (phaseData[2][i] - min[2]) * 255 / (max[2] - min[2]);
    }

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            phaseImage->setPixel(j, i, qRgb(phaseData[0][i * HEIGHT + j], phaseData[1][i * HEIGHT + j], phaseData[2][i * HEIGHT + j]));
        }
    }

    delete [] phaseData[0];
    delete [] phaseData[1];
    delete [] phaseData[2];
    delete [] phaseData;
}

void ToolFourier::createPhaseSpectrumWithMaskColor()
{
    phaseWithMaskImage = new QImage(WIDTH, HEIGHT, QImage::Format_RGB888);

    double **phaseWithMaskData = new double*[3];
    phaseWithMaskData[0] = new double[WIDTH * HEIGHT];
    phaseWithMaskData[1] = new double[WIDTH * HEIGHT];
    phaseWithMaskData[2] = new double[WIDTH * HEIGHT];

    moveToCorners(redOriginalFFT);
    moveToCorners(greenOriginalFFT);
    moveToCorners(blueOriginalFFT);
    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        // RED
        phaseWithMaskData[0][i] = std::arg(redOriginalFFT[i] * redMaskFFT[i]);
        // GREEN
        phaseWithMaskData[1][i] = std::arg(greenOriginalFFT[i] * greenMaskFFT[i]);
        //BLUE
        phaseWithMaskData[2][i] = std::arg(blueOriginalFFT[i] * blueMaskFFT[i]);
    }
    moveToCenter(redOriginalFFT);
    moveToCenter(greenOriginalFFT);
    moveToCenter(blueOriginalFFT);

    for (int i = 0; i < WIDTH * HEIGHT; i++) {
        phaseWithMaskData[0][i] = (phaseWithMaskData[0][i] - minValueForNormalizationColor[0]) * 255 / (maxValueForNormalizationColor[0] - minValueForNormalizationColor[0]);
        phaseWithMaskData[1][i] = (phaseWithMaskData[1][i] - minValueForNormalizationColor[1]) * 255 / (maxValueForNormalizationColor[1] - minValueForNormalizationColor[1]);
        phaseWithMaskData[2][i] = (phaseWithMaskData[2][i] - minValueForNormalizationColor[2]) * 255 / (maxValueForNormalizationColor[2] - minValueForNormalizationColor[2]);
    }

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            phaseWithMaskImage->setPixel(j, i, qRgb(phaseWithMaskData[0][i * HEIGHT + j], phaseWithMaskData[1][i * HEIGHT + j], phaseWithMaskData[2][i * HEIGHT + j]));
        }
    }

    delete [] phaseWithMaskData[0];
    delete [] phaseWithMaskData[1];
    delete [] phaseWithMaskData[2];
    delete [] phaseWithMaskData;
}
