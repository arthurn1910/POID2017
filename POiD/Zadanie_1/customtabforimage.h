#ifndef CUSTOMTABFORIMAGE_H
#define CUSTOMTABFORIMAGE_H

#include <QWidget>
#include <string>
#include <QGraphicsView>
#include <QGraphicsScene>
#include <QGraphicsPixmapItem>
#include <QFileDialog>
#include <QVBoxLayout>
#include "tools/tool.h"

namespace Ui {
class CustomTabForImage;
}

class CustomTabForImage : public QWidget
{
    Q_OBJECT

public:
    explicit CustomTabForImage(QWidget *parent = 0);
    explicit CustomTabForImage(QWidget *parent = 0, QString graphicFilePath = "");

    void loadFile();

    ~CustomTabForImage();

    QImage *getProcessedQImage();

private slots:
    void on_toolsComboBox_currentIndexChanged(int index);

    void on_transformButton_clicked();

    void on_restoreButton_clicked();

    void on_swapButton_clicked();

private:
    void cleanToolLayout();
    Tool *createTool(int index);
    const int TOOL_COUNT = 2;
    Tool *tool;

    QVBoxLayout *toolLayout;

    Ui::CustomTabForImage *ui;

    void setupQGraphicsViews();

    QImage *originalGrahpicImage;
    QGraphicsPixmapItem *originalGrahpicPixmap;
    QGraphicsView *originalGraphicView;
    QGraphicsScene *originalGrahpicScane;

    QImage *processedGrahpicImage = nullptr;
    QGraphicsPixmapItem *processedGrahpicPixmap;
    QGraphicsView *processedGraphicView;
    QGraphicsScene *processedGrahpicScane;

    QString graphicFilePath;
};

#endif // CUSTOMTABFORIMAGE_H
