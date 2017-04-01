#ifndef FiltrSobel_H
#define FiltrSobel_H

#include <QWidget>
#include <QLabel>
#include "../tool.h"

namespace Ui {
class FiltrSobel;
}

class FiltrSobel : public Tool
{
    Q_OBJECT
public:
    explicit FiltrSobel(QWidget *parent = 0);
    explicit FiltrSobel(int width, int height, int depth, QWidget *parent = 0);
    ~FiltrSobel();

    QImage* process(QImage *photoData);


private:
    Ui::FiltrSobel *ui;
};

#endif // FiltrSobel_H
