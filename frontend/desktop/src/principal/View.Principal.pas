unit View.Principal;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants, System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.ExtCtrls, Vcl.StdCtrls, Vcl.Buttons, Vcl.WinXCtrls;

type
  TFrmPrincipal = class(TForm)
    pnlTopo: TPanel;
    lblMenu: TLabel;
    lblTitulo: TLabel;
    lblUsuario: TLabel;
    lblIconeUsuario: TLabel;
    svMenu: TSplitView;
    btnInicio: TSpeedButton;
    btnArtistas: TSpeedButton;
    btnAlbuns: TSpeedButton;
    btnSair: TSpeedButton;
    pnlConteudo: TPanel;
    pnlCartao: TPanel;
    lblBemVindo: TLabel;
    procedure FormCreate(Sender: TObject);
    procedure lblMenuClick(Sender: TObject);
    procedure btnInicioClick(Sender: TObject);
    procedure btnArtistasClick(Sender: TObject);
    procedure btnAlbunsClick(Sender: TObject);
    procedure btnSairClick(Sender: TObject);
  private
    { Private declarations }
    procedure SelecionarModulo(const ATitulo: string);
  public
    { Public declarations }
  end;

var
  FrmPrincipal: TFrmPrincipal;

implementation

{$R *.dfm}

// Mostra o nome do módulo ativo na barra do topo, ao lado do menu.
procedure TFrmPrincipal.SelecionarModulo(const ATitulo: string);
begin
  lblTitulo.Caption := ATitulo;
end;

procedure TFrmPrincipal.FormCreate(Sender: TObject);
begin
  btnInicio.Click;
end;

procedure TFrmPrincipal.btnInicioClick(Sender: TObject);
begin
  SelecionarModulo(btnInicio.Caption);
end;

procedure TFrmPrincipal.btnArtistasClick(Sender: TObject);
begin
  SelecionarModulo(btnArtistas.Caption);
end;

procedure TFrmPrincipal.btnAlbunsClick(Sender: TObject);
begin
  SelecionarModulo(btnAlbuns.Caption);
end;

procedure TFrmPrincipal.lblMenuClick(Sender: TObject);
begin
  svMenu.Opened := not svMenu.Opened;
end;

procedure TFrmPrincipal.btnSairClick(Sender: TObject);
begin
  if MessageDlg('Deseja sair do sistema?', mtConfirmation, [mbYes, mbNo], 0) = mrYes then
    Close;
end;

end.
