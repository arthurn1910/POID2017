#ifndef FiltrLinearSplot_H
#define FiltrLinearSplot_H

#include <QWidget>
#include <QLabel>
#include "tool.h"

namespace Ui {
class FiltrLinearSplot;
}

class FiltrLinearSplot : public Tool
{
    Q_OBJECT
public:
    explicit FiltrLinearSplot(QWidget *parent = 0);
    explicit FiltrLinearSplot(int width, int height, int depth, QWidget *parent = 0);
    ~FiltrLinearSplot();

    QImage* process(QImage *photoData);
    //void setDefaultParameters();

private slots:
    void on_northRadioButton_clicked(bool checked);

    void on_northEastRadioButton_clicked(bool checked);

    void on_eastRadioButton_clicked(bool checked);

    void on_southEastRadioButton_clicked(bool checked);

private:
    int getWidthOfMaskSize();
    int getHeightOfMaskSize();
    int *parseFiltrMask(QString filtrMask);

    QLabel* consoleValue;
    Ui::FiltrLinearSplot *ui;
};

#endif // FiltrLinearSplot_H
