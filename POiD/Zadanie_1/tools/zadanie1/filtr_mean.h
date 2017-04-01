#ifndef FiltrMean_H
#define FiltrMean_H

#include <QWidget>
#include <QLabel>
#include <QFileDialog>
#include "../tool.h"

namespace Ui {
class FiltrMean;
}

class FiltrMean : public Tool
{
    Q_OBJECT
public:
    explicit FiltrMean(QWidget *parent = 0);
    explicit FiltrMean(int width, int height, int depth, QWidget *parent = 0);
    ~FiltrMean();

    QImage* process(QImage *photoData);

private slots:
    void on_errorButton_clicked();

private:
    int getWidthOfMaskSize();
    int getHeightOfMaskSize();
    void initializeImageFileDialog(QFileDialog &dialog, QFileDialog::AcceptMode acceptMode);
    QString selectOriginalFile();
    double calculateErrorFor8BitImage(QImage *original, QImage *other);
    double *calculateErrorFor32BitImage(QImage *original, QImage *other);

    QImage *processedImage;
    QImage *inputImage;
    Ui::FiltrMean *ui;
};

#endif // FiltrMean_H
