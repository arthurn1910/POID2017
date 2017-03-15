// This GUI file is generated by guibuilder version 4.0
//////////
f=figure('figure_position',[473,209],'figure_size',[360,384],'auto_resize','on','background',[33],'figure_name','Okno graficzne numer %d','dockable','off','infobar_visible','off','toolbar_visible','off','menubar_visible','off','default_axes','on','visible','off');
//////////
handles.dummy = 0;

handles.expresionLabel=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.0,0.9,0.23,0.09],'Relief','default','SliderStep',[0.01,0.1],'String',' Funkcja f(X):','Style','text','Value',[0],'VerticalAlignment','middle','Visible','on','Tag','expresionLabel','Callback','')
handles.expresionValue=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.25,0.9,0.4,0.09],'Relief','default','SliderStep',[0.01,0.1],'String','','Style','edit','Value',[0],'VerticalAlignment','middle','Visible','on','Tag','expresionValue','Callback','')

handles.rangeLabel=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.0,0.8,0.23,0.09],'Relief','default','SliderStep',[0.01,0.1],'String',' Przedział X:','Style','text','Value',[0],'VerticalAlignment','middle','Visible','on','Tag','rangeLabel','Callback','')
handles.rangeMin=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.25,0.8,0.2,0.09],'Relief','default','SliderStep',[0.01,0.1],'String','Od','Style','edit','Value',[0],'VerticalAlignment','middle','Visible','on','Tag','rangeMin','Callback','')
handles.rangeMax=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.45,0.8,0.2,0.09],'Relief','default','SliderStep',[0.01,0.1],'String','Do','Style','edit','Value',[0],'VerticalAlignment','middle','Visible','on','Tag','rangeMax','Callback','')

handles.maxIterationsLabel=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.0,0.7,0.23,0.09],'Relief','default','SliderStep',[0.01,0.1],'String',' Max iteracji:','Style','text','Value',[0],'VerticalAlignment','middle','Visible','on','Tag','rangeLabel','Callback','')
handles.maxIterationsValue=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.25,0.7,0.2,0.09],'Relief','default','SliderStep',[0.01,0.1],'String','100','Style','edit','Value',[0],'VerticalAlignment','middle','Visible','on','Tag','rangeMin','Callback','')
handles.precisionLabel=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.46,0.7,0.23,0.09],'Relief','default','SliderStep',[0.01,0.1],'String',' Dokładność:','Style','text','Value',[0],'VerticalAlignment','middle','Visible','on','Tag','rangeLabel','Callback','')
handles.precisionValue=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.7,0.7,0.2,0.09],'Relief','default','SliderStep',[0.01,0.1],'String','0.01','Style','edit','Value',[0],'VerticalAlignment','middle','Visible','on','Tag','rangeMin','Callback','')

handles.methodsLabel=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.0,0.6,0.35,0.07],'Relief','default','SliderStep',[0.01,0.1],'String',' Dostępne metody:','Style','text','Value',[0],'VerticalAlignment','middle','Visible','on','Tag','methodsLabel','Callback','')
handles.methodBisection=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.05,0.53,0.35,0.06],'Relief','default','SliderStep',[0.01,0.1],'String','metoda Bisekcji','Style','radiobutton','Groupname',"methodGroup",'Value',[0],'VerticalAlignment','middle','Visible','on','Tag','methodBisection','Callback','')
handles.methodGoldenSection=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.05,0.46,0.48,0.06],'Relief','default','SliderStep',[0.01,0.1],'String','metoda Złotego podziału','Style','radiobutton','Groupname',"methodGroup",'Value',[0],'VerticalAlignment','middle','Visible','on','Tag','methodGoldenSection','Callback','')

handles.assertLabel=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','bold','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.1,0.35,0.2,0.05],'Relief','default','SliderStep',[0.01,0.1],'String',' Asercja:','Style','text','Value',[0],'VerticalAlignment','middle','Visible','off','Tag','methodsLabel','Callback','')
handles.assertErrors=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','left','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.1,0.14,0.8,0.2],'Relief','default','SliderStep',[0.01,0.1],'String','','Style','listbox','Value',[0],'VerticalAlignment','middle','Visible','off','Tag','methodsLabel','Callback','')


handles.processButton=uicontrol(f,'unit','normalized','BackgroundColor',[-1,-1,-1],'Enable','on','FontAngle','normal','FontName','Tahoma','FontSize',[12],'FontUnits','points','FontWeight','normal','ForegroundColor',[-1,-1,-1],'HorizontalAlignment','center','ListboxTop',[],'Max',[1],'Min',[0],'Position',[0.25,0.02,0.5,0.1],'Relief','default','SliderStep',[0.01,0.1],'String','Oblicz','Style','pushbutton','Value',[0],'VerticalAlignment','middle','Visible','on','Tag','processButton','Callback','processButton_callback(handles)')

f.visible = "on";


//////////
// Callbacks are defined as below. Please do not delete the comments as it will be used in coming version
//////////
currentDirectory = get_absolute_file_path("zadanie_1.sce");
exec(currentDirectory + "\utils.sce");
exec(currentDirectory + "\bisection.sce");
exec(currentDirectory + "\golden_section.sce");
exec(currentDirectory + "\unimod_test.sce");

function processButton_callback(handles)
//Write your callback for  processButton  here
    stringEquation = handles.expresionValue.string;
    stringEquation = convstr(stringEquation, "l");
    
    rangeMin = strtod(handles.rangeMin.string);
    rangeMax = strtod(handles.rangeMax.string);
    
    maxIterations = strtod(handles.maxIterationsValue.string);
    precision = strtod(handles.precisionValue.string);
    
    useMethodBisection = handles.methodBisection.value;
    useMethodGoldenSection = handles.methodGoldenSection.value;
    
    handles.assertLabel.visible = "off"
    handles.assertErrors.visible = "off"
    [validInput, errorStrings] = assertInputData(stringEquation, rangeMin, rangeMax, useMethodBisection, useMethodGoldenSection, maxIterations, precision);
    
    if ~validInput then
        handles.assertLabel.visible = "on"
        handles.assertErrors.visible = "on"
        handles.assertErrors.string = errorStrings;
        return;
    end
    
    if testIfUnimodalFunction(stringEquation, rangeMin, rangeMax) then
        [rangeMin, rangeMax] = designateNewRangeForUnimodalFunction(stringEquation, rangeMin, rangeMax);
    end
    
    if useMethodBisection then
        bisection(stringEquation, rangeMin, rangeMax);
    else
        goldenSection(stringEquation, rangeMin, rangeMax);
    end

endfunction

