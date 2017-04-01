#define _USE_MATH_DEFINES

#include "tool_fourier.h"
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
#include <iostream>

void ToolFourier::FFT(ComplexArray *input)
{
    const size_t N = input->size();
    if (N <= 1)
        return;

    // divide
    ComplexArray even = (*input)[std::slice(0, N/2, 2)];
    ComplexArray  odd = (*input)[std::slice(1, N/2, 2)];


    // conquer
    FFT(&even);
    FFT(&odd);

    // combine
    for (int k = 0; k < N/2; k++)
    {
        Complex com;
        com.real(0);
        com.imag(-2 * M_PI * k / N);
        Complex t = std::exp(com) * odd[k];
        (*input)[k    ] = even[k] + t;
        (*input)[k+N/2] = even[k] - t;
    }

    return;
}

void ToolFourier::IFFT(ComplexArray *input)
{
    // conjugate the complex numbers
    *input = input->apply(std::conj);

    // forward fft
    FFT( input );

    // conjugate the complex numbers again
    *input = input->apply(std::conj);

    // scale the numbers
    *input /= input->size();
}
