#ifndef SpectrumWindow_H
#define SpectrumWindow_H

#include <QMainWindow>
#include <QGraphicsView>
#include <QGraphicsScene>
#include <QGraphicsPixmapItem>

namespace Ui {
class SpectrumWindow;
}

class SpectrumWindow : public QMainWindow
{
    Q_OBJECT
public:
    explicit SpectrumWindow(QWidget *parent = 0);
    ~SpectrumWindow();

    void setOriginalSpectrum(QImage *image);
    void setWithMaskSpectrum(QImage *image);

private:
    Ui::SpectrumWindow *ui;

    void setupQGraphicsViews();

    QImage *originalGrahpicImage;
    QGraphicsPixmapItem *originalGrahpicPixmap;
    QGraphicsView *originalGraphicView;
    QGraphicsScene *originalGrahpicScane;

    QImage *withMaskGrahpicImage = nullptr;
    QGraphicsPixmapItem *withMaskGrahpicPixmap;
    QGraphicsView *withMaskGraphicView;
    QGraphicsScene *withMaskGrahpicScane;
};

#endif // SpectrumWindow_H
