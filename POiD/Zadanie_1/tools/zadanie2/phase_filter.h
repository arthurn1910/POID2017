#ifndef PhaseFilter_H
#define PhaseFilter_H

#include "../tool_fourier.h"
#include <QWidget>
#include <QSlider>

namespace Ui {
class PhaseFilter;
}

class PhaseFilter :  public ToolFourier
{
    Q_OBJECT
public:
    explicit PhaseFilter(QWidget *parent = 0);
    explicit PhaseFilter(int width, int height, int depth, QWidget *parent = 0);
    ~PhaseFilter();

    QImage *process(QImage *image);
    void createMask(std::complex<double> *maskFFT);


private:
    int getPhaseFilterLevel();
    void updateLookUpTable();
    QSlider *slider;
    Ui::PhaseFilter *ui;
};

#endif // PhaseFilter_H
