#ifndef BRIGHTNESS_H
#define BRIGHTNESS_H

#include "../tool.h"
#include <QWidget>
#include <QSlider>

namespace Ui {
class Brightness;
}

class Brightness :  public Tool
{
public:
    explicit Brightness(QWidget *parent = 0);
    explicit Brightness(int width, int height, int depth, QWidget *parent = 0);
    ~Brightness();

    QImage* process(QImage *photoData);
    void setDefaultParameters();


private:
    int getBrightnessLevel();
    void updateLookUpTable();
    QSlider *slider;
    Ui::Brightness *ui;
};

#endif // BRIGHTNESS_H
