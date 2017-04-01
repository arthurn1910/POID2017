#ifndef CONTRAST_H
#define CONTRAST_H

#include <QWidget>
#include <QSlider>
#include "../tool.h"

namespace Ui {
class Contrast;
}

class Contrast : public Tool
{
public:
    explicit Contrast(QWidget *parent = 0);
    explicit Contrast(int width, int height, int depth, QWidget *parent = 0);
    ~Contrast();

    QImage* process(QImage *photoData);
    void setDefaultParameters();

private:
    int getContrastLevel();
    void updateLookUpTable();
    QSlider *slider;
    Ui::Contrast *ui;
};

#endif // CONTRAST_H
