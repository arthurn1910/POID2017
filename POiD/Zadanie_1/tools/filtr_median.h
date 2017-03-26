#ifndef FiltrMedian_H
#define FiltrMedian_H

#include <QWidget>
#include <QLabel>
#include "tool.h"

namespace Ui {
class FiltrMedian;
}

class FiltrMedian : public Tool
{
public:
    explicit FiltrMedian(QWidget *parent = 0);
    explicit FiltrMedian(int width, int height, int depth, QWidget *parent = 0);
    ~FiltrMedian();

    QImage* process(QImage *photoData);

private slots:
    void on_errorButton_clicked();

private:
    int getWidthOfMaskSize();
    int getHeightOfMaskSize();

    QImage *processedImage;
    QImage *inputImage;
    Ui::FiltrMedian *ui;
};

#endif // FiltrMedian_H
