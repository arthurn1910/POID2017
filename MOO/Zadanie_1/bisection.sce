
function bisection(equation, rangeMin, rangeMax,maxIterations,precision)
    currentRangeMin=rangeMin;
    currentRangeMax=rangeMax
    currentIteration=1;
    
    range=rangeMax-rangeMin;
    delta=range/8;
    
    mprintf("\n-------------------------------------\n");
    mprintf("Funkcja: %s. Przedział: [%f , %f].\n", equation, currentRangeMin, currentRangeMax);
    mprintf("Max iteracji: %i, Dokładność: %f.\n", maxIterations, precision);
    
    scf();
    x=[rangeMin:0.1:rangeMax];
    plot(x,eval(equation),'r');
    title(equation);
    xlabel('x'); ylabel('y');
    for i=0:6
        u0=rangeMin+i*delta;
        u1=rangeMin+i*delta+delta;
        u2=rangeMin++i*delta+2*delta;
        x=u0;
        fu0=evstr(equation);
        x=u1;
        fu1=evstr(equation);
        x=u2;
        fu2=evstr(equation);
        if((fu0>=fu1) & (fu1<=fu2)) then
           currentRangeMin=u0;
           currentRangeMax=u2;
           plot(currentRangeMin,0.1,'co');
           plot(currentRangeMax,0.1,'co');
           break; 
        end
        
    end
    mprintf("Przedzial unimodalny: %f, %f \n",currentRangeMin, currentRangeMax);    
    

    while currentIteration <= maxIterations
        if((currentRangeMax -currentRangeMin)>precision) then
            mprintf("Iteracja: %i. Badany przedział: [ %f : %f ]. Dokładność: %f\n",currentIteration,currentRangeMin,currentRangeMax, (currentRangeMax-currentRangeMin)/2);
            if(currentIteration>1) then
               plot(currentRangeMin,0.25*currentIteration,'o');
               plot(currentRangeMax,0.25*currentIteration,'o');
            end 
            L=(currentRangeMax-currentRangeMin)/4;
            x0=currentRangeMin+2*L;
            x1=currentRangeMin+L;
            x2=currentRangeMin+3*L;
            x=x0;
            f0=evstr(equation);
            x=x1;
            f1=evstr(equation);
            x=x2;
            f2=evstr(equation);
            
            if((f1<f0) & (f0<f2)) then
                currentRangeMax=x0;
                x0=x1;
            else 
                currentRangeMin=x0;
                x0=x2;
            end 
        else
           break; 
        end
        currentIteration=currentIteration+1;
    end

    mprintf("Ostateczny osiągnięty przedział: %f, %f \n",currentRangeMin, currentRangeMax); 
    mprintf("Środek przedziału: %f\n",(currentRangeMin + currentRangeMax)/2);
    mprintf("Bład +- %f\n",((currentRangeMin + currentRangeMax)/2)-currentRangeMin);
    mprintf("-------------------------------------\n");
endfunction
