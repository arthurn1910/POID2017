#define _USE_MATH_DEFINES

#include "tool_fourier.h"
#include "zadanie2/spectrum_window.h"
#include <cmath>

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

QImage *ToolFourier::process8BitImage(QImage *originalPhoto)
{
    QImage *processedImage;
    const uchar *photoPixels = originalPhoto->bits();
    uchar *processedPhotoPixels = new uchar[WIDTH * HEIGHT];
    std::complex<double> *grayWithMaskFFT = new std::complex<double>[WIDTH * HEIGHT];

    if (grayOriginalFFT == nullptr) {
        grayOriginalFFT = new std::complex<double>[WIDTH * HEIGHT];

        for (int i = 0; i < WIDTH*HEIGHT; i++) {
            grayOriginalFFT[i].real((double) photoPixels[i]);
        }

        FFT(grayOriginalFFT, WIDTH * HEIGHT);

        moveToCenter(grayOriginalFFT);
    }

    applayMask(grayOriginalFFT, grayWithMaskFFT);

    moveToCorners(grayWithMaskFFT);

    IFFT(grayWithMaskFFT, WIDTH * HEIGHT);

    for (int i = 0; i < WIDTH*HEIGHT; i++) {
        processedPhotoPixels[i] = (uchar) grayWithMaskFFT[i].real();
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
    std::complex<double> *redWithMaskFFT = new std::complex<double>[WIDTH * HEIGHT];
    std::complex<double> *greenWithMaskFFT = new std::complex<double>[WIDTH * HEIGHT];
    std::complex<double> *blueWithMaskFFT = new std::complex<double>[WIDTH * HEIGHT];

    if (redOriginalFFT == nullptr) {
        // RED
        redOriginalFFT = new std::complex<double>[WIDTH * HEIGHT];
        for (int i = 0; i < WIDTH*HEIGHT; i++) {
            redOriginalFFT[i].real(qRed(photoPixels[i]));
        }
        FFT(redOriginalFFT, WIDTH * HEIGHT);
        moveToCenter(redOriginalFFT);

        // GREEN
        greenOriginalFFT = new std::complex<double>[WIDTH * HEIGHT];
        for (int i = 0; i < WIDTH*HEIGHT; i++) {
            greenOriginalFFT[i].real(qGreen(photoPixels[i]));
        }
        FFT(greenOriginalFFT, WIDTH * HEIGHT);
        moveToCenter(greenOriginalFFT);

        //BLUE
        blueOriginalFFT = new std::complex<double>[WIDTH * HEIGHT];
        for (int i = 0; i < WIDTH*HEIGHT; i++) {
            blueOriginalFFT[i].real(qBlue(photoPixels[i]));
        }
        FFT(blueOriginalFFT, WIDTH * HEIGHT);
        moveToCenter(blueOriginalFFT);
    }

    applayMask(redOriginalFFT, redWithMaskFFT);
    applayMask(greenOriginalFFT, greenWithMaskFFT);
    applayMask(blueOriginalFFT, blueWithMaskFFT);

    moveToCorners(redWithMaskFFT);
    moveToCorners(greenWithMaskFFT);
    moveToCorners(blueWithMaskFFT);

    IFFT(redWithMaskFFT, WIDTH * HEIGHT);
    IFFT(greenWithMaskFFT, WIDTH * HEIGHT);
    IFFT(blueWithMaskFFT, WIDTH * HEIGHT);

    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            processedImage->setPixel(j, i, qRgb(redWithMaskFFT[i * HEIGHT + j].real(), greenWithMaskFFT[i * HEIGHT + j].real(), blueWithMaskFFT[i * HEIGHT + j].real()));
        }
    }

    delete [] redWithMaskFFT;
    delete [] greenWithMaskFFT;
    delete [] blueWithMaskFFT;

    return processedImage;
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

void ToolFourier::on_amplitudeButton_clicked()
{
    if (DEPTH == 8) {
        double *amplitudeData = new double[WIDTH * HEIGHT];
        uchar *imageData = new uchar[WIDTH * HEIGHT];

        double max = 0;
        double min = 255;
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            amplitudeData[i] = std::log(1 + std::abs(grayOriginalFFT[i]));

            if (max < amplitudeData[i]) {
                max = amplitudeData[i];
            }
            if (min > amplitudeData[i]) {
                min = amplitudeData[i];
            }
        }

        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            imageData[i] = (amplitudeData[i] - min) * 255 / (max - min);
        }

        amplitudeImage = new QImage(imageData, WIDTH, HEIGHT, WIDTH, QImage::Format_Grayscale8);
        for (int i = 0; i < 256; i++) {
            amplitudeImage->setColor(i, qRgb(i, i, i));
        }
    } else {
        amplitudeImage = new QImage(WIDTH, HEIGHT, QImage::Format_RGB888);

        double **amplitudeData = new double*[3];
        amplitudeData[0] = new double[WIDTH * HEIGHT];
        amplitudeData[1] = new double[WIDTH * HEIGHT];
        amplitudeData[2] = new double[WIDTH * HEIGHT];

        double max[3] {0};
        double min[3] {255};
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            // RED
            amplitudeData[0][i] = std::log(1 + std::abs(redOriginalFFT[i]));

            if (max[0] < amplitudeData[0][i]) {
                max[0] = amplitudeData[0][i];
            }
            if (min[0] > amplitudeData[0][i]) {
                min[0] = amplitudeData[0][i];
            }
            // GREEN
            amplitudeData[1][i] = std::log(1 + std::abs(greenOriginalFFT[i]));

            if (max[1] < amplitudeData[1][i]) {
                max[1] = amplitudeData[1][i];
            }
            if (min[1] > amplitudeData[1][i]) {
                min[1] = amplitudeData[1][i];
            }
            //BLUE
            amplitudeData[2][i] = std::log(1 + std::abs(blueOriginalFFT[i]));

            if (max[2] < amplitudeData[2][i]) {
                max[2] = amplitudeData[2][i];
            }
            if (min[2] > amplitudeData[2][i]) {
                min[2] = amplitudeData[2][i];
            }
        }

        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            amplitudeData[0][i] = (amplitudeData[0][i] - min[0]) * 255 / (max[0] - min[0]);
            amplitudeData[1][i] = (amplitudeData[1][i] - min[1]) * 255 / (max[1] - min[1]);
            amplitudeData[2][i] = (amplitudeData[2][i] - min[2]) * 255 / (max[2] - min[2]);
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

    SpectrumWindow *window = new SpectrumWindow(this);
    window->setOriginalSpectrum(amplitudeImage);
    window->show();
}

void ToolFourier::on_phaseButton_clicked()
{
    if (DEPTH == 8) {
        double *phaseData = new double[WIDTH * HEIGHT];
        uchar *imageData = new uchar[WIDTH * HEIGHT];

        double max = 0;
        double min = 255;

        moveToCorners(grayOriginalFFT);
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            phaseData[i] = std::arg(grayOriginalFFT[i]);

            if (max < phaseData[i]) {
                max = phaseData[i];
            }
            if (min > phaseData[i]) {
                min = phaseData[i];
            }
        }
        moveToCenter(grayOriginalFFT);

        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            imageData[i] = (phaseData[i] - min) * 255 / (max - min);
        }

        phaseImage = new QImage(imageData, WIDTH, HEIGHT, WIDTH, QImage::Format_Grayscale8);
        for (int i = 0; i < 256; i++) {
            phaseImage->setColor(i, qRgb(i, i, i));
        }
    } else {
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

    SpectrumWindow *window = new SpectrumWindow(this);
    window->setOriginalSpectrum(phaseImage);
    window->show();
}
