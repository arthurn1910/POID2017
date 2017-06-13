function [1]=Zadanie3(equation, rangeXMin, rangeXMax,rangeYMin, rangeYMax,maxIterations,precision)
   
    /*rangeXMin=-2;
    rangeXMax=2;
    rangeYMin=-2;
    rangeYMax=2;
    
    
    vAlfa = 1;
    vBeta = 2;
    vPsi = 0.5;
    vSigma=0.5;*/
    
    vAlfa = 1.5;
    vBeta = 1;
    vPsi = 0.5;
    vSigma=0.5;
    vDistance=(rangeXMax-rangeXMin)/10;
    
    currentIteration=1;
    
    rangeX=rangeXMax-rangeXMin;
    rangeY=rangeYMax-rangeYMin;
   
   
   xVector=[]
   yVector=[]
   zVector=[]
        
    scf();
    
    deff('[z]=f(x,y)', 'z='+equation)
    xp1=6.615244;
    yp1=5.135672;
    x=xp1;
    y=yp1;
    zp1=feval(x,y,f)
    xp2=xp1-vDistance/4;
    if((yp1+vDistance)>rangeYMax) then
        yp2=yp1-vDistance;
    else
        yp2=yp1+vDistance
    end
    x=xp2;
    y=yp2;
    zp2=feval(x,y,f)
    vDistanceTop=sqrt((xp1-xp2)^2+(yp1-yp2)^2)
    [xp3,yp3]=find3thTopyui(xp1,yp1,xp2,yp2,vDistanceTop,equation);
    x=xp3;
    y=yp3;
    zp3=feval(x,y,f)
    mprintf("\n!!!!!!! Wierzcholek 1: %f,%f\n\n",xp1,yp1)
    mprintf("!!!!!!! Wierzcholek 2: %f,%f\n\n",xp2,yp2)
    mprintf("!!!!!!! Wierzcholek 3: %f,%f\n\n",xp3,yp3)
    
    x=[rangeXMin:0.1:rangeXMax];
    y=[rangeYMin:0.1:rangeYMax];
    
    plot3d1(x,y,list(feval(x,y,f),  [color("red")*x]));
                
    if(zp1<=zp2)then
        if(zp1<=zp3) then
            xl=xp1;
            yl=yp1;
            zl=zp1;
            if(zp2<=zp3) then
                xh=xp3;
                yh=yp3;
                zh=zp3;
                xd=xp2;
                yd=yp2;
                zd=zp2;
            else
                xh=xp2;
                yh=yp2;
                zh=zp2;
                xd=xp3;
                yd=yp3;
                zd=zp3;
            end
        else
            xl=xp3;
            yl=yp3;
            zl=zp3;
            xh=xp2;
            yh=yp2;
            zh=zp2;
            xd=xp1;
            yd=yp1;
            zd=zp1;
        end
    else
        if(zp2<=zp3) then
            xl=xp2;
            yl=yp2;
            zl=zp2;
            if(zp1<=zp3) then
                xh=xp3;
                yh=yp3;
                zh=zp3;
                xd=xp1;
                yd=yp1;
                zd=zp1;
            else
                xh=xp1;
                yh=yp1;
                zh=zp1;
                xd=xp3;
                yd=yp3;
                zd=zp3;
            end
        else
            xl=xp3;
            yl=yp3;
            zl=zp3;
            xh=xp1;
            yh=yp1;
            zh=zp1;
            xd=xp2;
            yd=yp2;
            zd=zp2;
        end  
    end
    

    //p1=(xh-xl)/100
    //p2=(yh-xl)/100
    //mprintf("\n p1: %f, p2: %f, xl: %f, xh: %f, yl: %f, yh: %f \n",p1,p2,xl,xh,yl,yh)
    //mprintf("%f, %f, %f", yp1, yp2, yp3)
    //x=[xl:p1:xh];
    //y=[yl:p2:yh];
    //mprintf("x: %f\n\n y: %f \n",x,y)
    //mprintf("\n z: %f \n", list(feval(x,y,f)))
    //plot3d(x,y,list(feval(x,y,f)))
    
      
            
    while currentIteration <= maxIterations 
        if(sqrt((xh-xl)^2+(yh-yl)^2)>precision) then
            if(zh<zl) then
                xr=xl;
                yr=yl;
                zr=zl;
                xl=xh;
                yl=yh;
                zl=zh;
                xh=xd;
                yh=yd;
                zh=zd;
                xd=xr;
                yd=yr;
                zd=zr;
            elseif(zh<zd) then
                xr=xh;
                yr=yh;
                zr=zh;
                xh=xd;
                yh=yd;
                zh=zd;
                xd=xr;
                yd=yr;
                zd=zr;    
            end
            xVector=[xVector xl]
            yVector=[yVector yl]
            zVector=[zVector zl]           
            
            
            mprintf("\nIteracja: %i. Punkt l: [ %f : %f ] Wartosc: %f. Punkt d: [ %f : %f] Wartosc: %f. Punkt h: [ %f : %f ] Wartosc: %f.\n\n\n",currentIteration,xl,yl,zl,xd,yd,zd,xh,yh,zh);
           //  Punkt 4       
            xs=(xl+xd)/2;
            ys=(yl+yd)/2;
            x=xs;
            y=ys;
            zs=feval(x,y,f)
           
           
            xt=(1+vAlfa)*xs-vAlfa*xh;
            yt=(1+vAlfa)*ys-vAlfa*yh;
            
            x=xt;
            y=yt;
            zt=feval(x,y,f);
            /*mprintf("\n vvv \n")
            vDistanceTop1=sqrt((xt-xs)^2+(yt-ys)^2)
            vDistanceTop2=sqrt((xs-xh)^2+(ys-yh)^2)
            mprintf("\n odleglsoc %f, %f\n",vDistanceTop1,vDistanceTop2)
            mprintf("\n Ph: %f, %f, %f  Ps: %f, %f, %f  Pt: %f, %f, %f",xh,yh,zh,xs,ys,zs,xt,yt,zt)*/
            
                    
            if(zt<zl) then
                xe=(1+vPsi)*xt-vPsi*xs;
                ye=(1+vPsi)*yt-vPsi*ys;
                x=xe;
                y=ye
                ze=feval(x,y,f);
                if(ze<zl) then
                    
                    xh=xe;
                    yh=ye;
                    zh=ze;
                else 
                    
                    xh=xt;
                    yh=yt;
                    zh=zt;
                end
                
            else
                //Punkt 8
                if(zt>=zd) then
                    if(zt<zh) then
                        xh=xt;
                        yh=yt;
                        xh=xt;
                    end
                end
                xk=vBeta*xh+(1-vBeta)*xs;
                yk=vBeta*yh+(1-vBeta)*ys;  
                x=xk;
                y=yk;
                zk=feval(x,y,f);
                
                if(zk>=zh) then
                    
                    xh=(xh+xl)/2;
                    yh=(yh+yl)/2;
                    x=xh;
                    y=yh;
                    zh=feval(x,y,f);
                    
                    xd=(xd+xl)/2;
                    yd=(yd+yl)/2;
                    x=xd;
                    y=yd;
                    zd=feval(x,y,f);
                    //Punkt 10 
                    if(zt<zd) then
                    
                        xh=xt;
                        yh=yt;
                        zh=zt;
                    end
                    
                else
                    xh=xk;
                    yh=yk;
                    zh=zk;
                end
            end
        else
           break; 
        end
        currentIteration=currentIteration+1;
    end

    scatter3(xVector,yVector, zVector, ".");
    currentIteration=currentIteration-1;
    mprintf("Ostateczny zneleziony punkt (%f,%f,%f) w iteracji: %i",xl, yl,zl,currentIteration); 
    /*t = 1:200
x = t .* cos(t) .* sin(t)
y = t .* sin(t) .* sin(t)
z = t .* cos(t)
// create 3D scatter plot
scatter3(x, y, z)*/
// modify rotation angles
gca().rotation_angles = [60, 45];
endfunction
