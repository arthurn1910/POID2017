#ifndef Negative_H
#define Negative_H

#include "../tool.h"
#include <QWidget>
#include <QSlider>

namespace Ui {
class Negative;
}

class Negative :  public Tool
{
public:
    explicit Negative(QWidget *parent = 0);
    explicit Negative(int width, int height, int depth, QWidget *parent = 0);
    ~Negative();

    QImage* process(QImage *photoData);


private:
    void updateLookUpTable();
    Ui::Negative *ui;
};

#endif // Negative_H
