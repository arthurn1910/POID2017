#ifndef ToolFourier_H
#define ToolFourier_H

#include <QWidget>
#include <complex>
#include "tool.h"


class ToolFourier : public Tool
{
    Q_OBJECT
public:
    explicit ToolFourier(QWidget *parent = 0);
    explicit ToolFourier(int width, int height, int depth, QWidget *parent = 0);
    ~ToolFourier();
    QImage* process(QImage *photoData);


protected:
    void FFT(std::complex<double> *input, const int size);
    void IFFT(std::complex<double> *input, const int size);

    QImage *process8BitImage(QImage *originalPhoto);
    QImage *process32BitImage(QImage *originalPhoto);

    void moveToCenter(std::complex<double> *fftArray);
    void moveToCorners(std::complex<double> *fftArray);

    virtual std::complex<double> *applayMask(const std::complex<double> *originalFFT, std::complex<double> *maskFFT) = 0;

    std::complex<double> *grayOriginalFFT = nullptr;
    std::complex<double> *grayMaskFFT = nullptr;

    std::complex<double> *redOriginalFFT = nullptr;
    std::complex<double> *redMaskFFT = nullptr;

    std::complex<double> *greenOriginalFFT = nullptr;
    std::complex<double> *greenMaskFFT = nullptr;

    std::complex<double> *blueOriginalFFT = nullptr;
    std::complex<double> *blueMaskFFT = nullptr;

    QImage *amplitudeImage;
    QImage *amplitudeWithMaskImage;
    QImage *phaseImage;
    QImage *phaseWithMaskImage;

private slots:
    void on_amplitudeButton_clicked();

    void on_phaseButton_clicked();
};

#endif // ToolFourier_H
