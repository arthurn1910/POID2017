#ifndef ToolFourier_H
#define ToolFourier_H

#include <QWidget>
#include <complex>
#include <valarray>
#include "tool.h"


typedef std::complex<double> Complex;
typedef std::valarray<Complex> ComplexArray;

class ToolFourier : public Tool
{
    Q_OBJECT
public:
    explicit ToolFourier(QWidget *parent = 0);
    explicit ToolFourier(int width, int height, int depth, QWidget *parent = 0);
    ~ToolFourier();


protected:
    void FFT(ComplexArray *input);
    void IFFT(ComplexArray *input);

    ComplexArray *grayFourierArray = nullptr;
    ComplexArray *redFourierArray = nullptr;
    ComplexArray *greenFourierArray = nullptr;
    ComplexArray *blueFourierArray = nullptr;
};

#endif // ToolFourier_H
