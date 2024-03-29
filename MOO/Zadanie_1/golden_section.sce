
function goldenSection(equation, rangeMin, rangeMax, maxIterations, minRange)
    currentRangeMin=rangeMin;
    currentRangeMax=rangeMax;
    currentIteration=1;
    k=(sqrt(5)-1)/2;

    
    mprintf("\n-------------------------------------\n");
    mprintf("Funkcja: %s. Przedział: [%f , %f].\n", equation, currentRangeMin, currentRangeMax);
    mprintf("Max iteracji: %i, Dokładność: %f.\n", maxIterations, precision);

    range=rangeMax-rangeMin;
    delta=range/8;
    
    scf();
    x=[rangeMin:0.1:rangeMax];
    disp(equation);
    plot(x,eval(equation),'r');
    title(equation);
    xlabel('x'); ylabel('y');
    
    for i=0:6
        // lewa strona
        u0=rangeMin+i*delta;
        x=u0;
        fu0=evstr(equation);
        
        // środek
        u1=rangeMin+(i+1)*delta;
        x=u1;
        fu1=evstr(equation);
        
        // prawa strona
        u2=rangeMin+(i+2)*delta;        
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
    
    x1 = currentRangeMax - k*(currentRangeMax - currentRangeMin);
    x2 = currentRangeMin + k*(currentRangeMax - currentRangeMin);

    while currentIteration <= maxIterations
        if((currentRangeMax -currentRangeMin)>precision) then
            mprintf("Iteracja: %i. Badany przedział: [ %f : %f ]. Dokładność: %f\n",currentIteration,currentRangeMin,currentRangeMax, (currentRangeMax-currentRangeMin)/2);
            if(currentIteration>1) then
               plot(currentRangeMin,0.25*currentIteration,'o');
               plot(currentRangeMax,0.25*currentIteration,'o');
            end
            x = x1;
            f1 = evstr(equation);
            x = x2;
            f2 = evstr(equation);
            
            if(f2 < f1) then
                currentRangeMin = x1;
                x1=x2;
                x2=currentRangeMin + k*(currentRangeMax - currentRangeMin);
            else
                currentRangeMax = x2;
                x2=x1;
                x1=currentRangeMax - k*(currentRangeMax - currentRangeMin);
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
