object FrmPrincipal: TFrmPrincipal
  Left = 0
  Top = 0
  Caption = 'Cat'#225'logo de Artistas e '#193'lbuns'
  ClientHeight = 729
  ClientWidth = 1008
  Color = clBtnFace
  Constraints.MinHeight = 768
  Constraints.MinWidth = 1024
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -12
  Font.Name = 'Segoe UI'
  Font.Style = []
  Position = poScreenCenter
  WindowState = wsMaximized
  TextHeight = 15
  object pnlTopo: TPanel
    Left = 0
    Top = 0
    Width = 1008
    Height = 56
    Align = alTop
    BevelOuter = bvNone
    Color = 8543035
    ParentBackground = False
    ShowCaption = False
    TabOrder = 0
    StyleElements = [seFont, seBorder]
    ExplicitWidth = 1006
    object lblMenu: TLabel
      Left = 0
      Top = 0
      Width = 56
      Height = 56
      Cursor = crHandPoint
      Align = alLeft
      Alignment = taCenter
      AutoSize = False
      Caption = #59136
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWhite
      Font.Height = -20
      Font.Name = 'Segoe MDL2 Assets'
      Font.Style = []
      Layout = tlCenter
      ParentFont = False
      StyleElements = [seClient, seBorder]
      OnClick = lblMenuClick
    end
    object lblTitulo: TLabel
      AlignWithMargins = True
      Left = 64
      Top = 0
      Width = 400
      Height = 56
      Margins.Left = 8
      Margins.Top = 0
      Margins.Bottom = 0
      Align = alLeft
      AutoSize = False
      Caption = 'Cat'#225'logo de Artistas e '#193'lbuns'
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWhite
      Font.Height = -19
      Font.Name = 'Segoe UI'
      Font.Style = [fsBold]
      ParentFont = False
      Layout = tlCenter
      StyleElements = [seClient, seBorder]
    end
    object lblUsuario: TLabel
      AlignWithMargins = True
      Left = 864
      Top = 0
      Width = 92
      Height = 56
      Margins.Top = 0
      Margins.Right = 16
      Margins.Bottom = 0
      Align = alRight
      Alignment = taRightJustify
      AutoSize = False
      Caption = 'Ol'#225', Thiago'
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWhite
      Font.Height = -16
      Font.Name = 'Segoe UI'
      Font.Style = []
      ParentFont = False
      Layout = tlCenter
      StyleElements = [seClient, seBorder]
      ExplicitLeft = 872
    end
    object lblIconeUsuario: TLabel
      Left = 836
      Top = 0
      Width = 28
      Height = 56
      Align = alRight
      Alignment = taCenter
      AutoSize = False
      Caption = #59259
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWhite
      Font.Height = -20
      Font.Name = 'Segoe MDL2 Assets'
      Font.Style = []
      Layout = tlCenter
      ParentFont = False
      StyleElements = [seClient, seBorder]
      ExplicitLeft = 844
    end
  end
  object svMenu: TSplitView
    Left = 0
    Top = 56
    Width = 240
    Height = 673
    Color = 14603467
    OpenedWidth = 240
    Placement = svpLeft
    TabOrder = 1
    StyleElements = [seFont, seBorder]
    ExplicitHeight = 665
    object btnInicio: TSpeedButton
      Left = 0
      Top = 0
      Width = 240
      Height = 48
      Align = alTop
      Caption = 'In'#237'cio'
      Flat = True
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWindowText
      Font.Height = -16
      Font.Name = 'Segoe UI'
      Font.Style = []
      Margin = 24
      ParentFont = False
      ExplicitTop = 16
    end
    object btnArtistas: TSpeedButton
      Left = 0
      Top = 48
      Width = 240
      Height = 48
      Align = alTop
      Caption = 'Artistas'
      Flat = True
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWindowText
      Font.Height = -16
      Font.Name = 'Segoe UI'
      Font.Style = []
      Margin = 24
      ParentFont = False
      ExplicitTop = 64
    end
    object btnAlbuns: TSpeedButton
      Left = 0
      Top = 96
      Width = 240
      Height = 48
      Align = alTop
      Caption = #193'lbuns'
      Flat = True
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWindowText
      Font.Height = -16
      Font.Name = 'Segoe UI'
      Font.Style = []
      Margin = 24
      ParentFont = False
      ExplicitTop = 112
    end
    object btnSair: TSpeedButton
      Left = 0
      Top = 625
      Width = 240
      Height = 48
      Align = alBottom
      Caption = 'Sair'
      Flat = True
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWindowText
      Font.Height = -16
      Font.Name = 'Segoe UI'
      Font.Style = []
      Margin = 24
      ParentFont = False
      OnClick = btnSairClick
      ExplicitTop = 609
    end
  end
  object pnlConteudo: TPanel
    Left = 240
    Top = 56
    Width = 768
    Height = 673
    Align = alClient
    BevelOuter = bvNone
    Color = 15393500
    Padding.Left = 24
    Padding.Top = 24
    Padding.Right = 24
    Padding.Bottom = 24
    ParentBackground = False
    ShowCaption = False
    TabOrder = 2
    StyleElements = [seFont, seBorder]
    ExplicitWidth = 766
    ExplicitHeight = 665
    object pnlCartao: TPanel
      Left = 24
      Top = 24
      Width = 720
      Height = 625
      Align = alClient
      BevelOuter = bvNone
      Color = 15920617
      ParentBackground = False
      ShowCaption = False
      TabOrder = 0
      StyleElements = [seFont, seBorder]
      ExplicitWidth = 718
      ExplicitHeight = 617
      object lblBemVindo: TLabel
        Left = 32
        Top = 32
        Width = 260
        Height = 25
        Caption = 'Selecione uma op'#231#227'o no menu'
        Font.Charset = DEFAULT_CHARSET
        Font.Color = clGrayText
        Font.Height = -19
        Font.Name = 'Segoe UI'
        Font.Style = []
        ParentFont = False
      end
    end
  end
end
