#include "filtr_median.h"
#include "ui_filtr_median.h"
#include <vector>
#include <algorithm>
#include "utils.h"

FiltrMedian::FiltrMedian(QWidget *parent) :
    Tool(parent),
    ui(new Ui::FiltrMedian)
{
    ui->setupUi(this);
}

FiltrMedian::FiltrMedian(int width, int height, int depth, QWidget *parent) :
    Tool(width, height, depth, parent),
    ui(new Ui::FiltrMedian)
{
    ui->setupUi(this);
}

FiltrMedian::~FiltrMedian()
{
    delete ui;
}

QImage *FiltrMedian::process(QImage *image)
{
    inputImage = image;

    const int maskWidth = getWidthOfMaskSize();
    const int maskHeight = getHeightOfMaskSize();

    int maskPixelCount = 0;
    int sumOfSurroundingPixels = 0;


    if (DEPTH == 8) {
        std::vector<int> pixelsInMaskVector;
        const uchar *photoData = image->bits();
        uchar *processedData = new uchar[WIDTH * HEIGHT];


        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i - maskHeight < 0 || i + maskHeight >= HEIGHT || j - maskWidth < 0 || j + maskWidth >= WIDTH) {
                    processedData[i * HEIGHT + j] = photoData[i * HEIGHT + j];
                    continue;
                }
                sumOfSurroundingPixels = 0;
                maskPixelCount = 0;
                pixelsInMaskVector.clear();
                for (int m = -maskHeight; m <= maskHeight; m++) {
                    for (int n = -maskWidth; n <= maskWidth; n++) {                        
                        pixelsInMaskVector.push_back(photoData[(i+m) * HEIGHT + (j+n)]);
                        maskPixelCount++;
                    }
                }
                // Slow ~1500-2000 nanoseconds
                //std::nth_element(pixelsInMaskVector.begin(), pixelsInMaskVector.begin()+(maskPixelCount / 2), pixelsInMaskVector.end());
                //processedData[i * HEIGHT + j] = (uchar) pixelsInMaskVector[maskPixelCount / 2];

                // Fast ~500-900 nanoseconds
                int *temp = &pixelsInMaskVector[0];
                int median = heapMedian3(temp, maskPixelCount);
                processedData[i * HEIGHT + j] = (uchar) median;
            }
        }
        processedImage = new QImage(processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
        for (int i = 0; i < 256; i++) {
            processedImage->setColor(i, qRgb(i, i, i));
        }
    } else {
        // Obrazy 24 bitowe czyta jako 32...        

        std::vector<int> sumOfSurroundingRedPixels;
        std::vector<int> sumOfSurroundingGreenPixels;
        std::vector<int> sumOfSurroundingBluePixels
                ;
        const QRgb *photoData = (const QRgb *) image->bits();
        QRgb *processedData = new QRgb[WIDTH * HEIGHT];


        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i - maskHeight < 0 || i + maskHeight >= HEIGHT || j - maskWidth < 0 || j + maskWidth >= WIDTH) {
                    processedData[i * HEIGHT + j] = photoData[i * HEIGHT + j];
                    continue;
                }
                sumOfSurroundingRedPixels.clear();
                sumOfSurroundingGreenPixels.clear();
                sumOfSurroundingBluePixels.clear();
                maskPixelCount = 0;
                for (int m = -maskHeight; m <= maskHeight; m++) {
                    for (int n = -maskWidth; n <= maskWidth; n++) {
                        QRgb currentPixel = photoData[(i+m) * HEIGHT + (j+n)];
                        sumOfSurroundingRedPixels.push_back(qRed(currentPixel));
                        sumOfSurroundingGreenPixels.push_back(qGreen(currentPixel));
                        sumOfSurroundingBluePixels.push_back(qBlue(currentPixel));
                        maskPixelCount++;
                    }
                }
                int *temp = &sumOfSurroundingRedPixels[0];
                int medianRed = heapMedian3(temp, maskPixelCount);
                temp = &sumOfSurroundingGreenPixels[0];
                int medianGreen = heapMedian3(temp, maskPixelCount);
                temp = &sumOfSurroundingBluePixels[0];
                int medianBlue = heapMedian3(temp, maskPixelCount);

                processedData[i * HEIGHT + j] = qRgb(medianRed, medianGreen, medianBlue);
            }
        }
        processedImage = new QImage((uchar *)processedData, WIDTH, HEIGHT, image->bytesPerLine(), image->format());
    }

    return processedImage;
}

int FiltrMedian::getWidthOfMaskSize()
{
    return ui->widthValue->value();
}

int FiltrMedian::getHeightOfMaskSize()
{
    return ui->heightValue->value();
}


void FiltrMedian::on_errorButton_clicked()
{

}
