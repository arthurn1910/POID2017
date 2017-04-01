#ifndef LowPassFilter_H
#define LowPassFilter_H

#include "../tool_fourier.h"
#include <QWidget>
#include <QSlider>

namespace Ui {
class LowPassFilter;
}

class LowPassFilter :  public ToolFourier
{
    Q_OBJECT
public:
    explicit LowPassFilter(QWidget *parent = 0);
    explicit LowPassFilter(int width, int height, int depth, QWidget *parent = 0);
    ~LowPassFilter();

    QImage* process(QImage *photoData);
    void setDefaultParameters();


private slots:
    void on_fourierButton_clicked();

private:
    int getLowPassFilterLevel();
    void updateLookUpTable();
    QSlider *slider;
    Ui::LowPassFilter *ui;
};

#endif // LowPassFilter_H
